package com.wecome.demo.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Region;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.wecome.demo.AppConfig;
import com.wecome.demo.model.NaviModel;
import com.wecome.demo.utils.ScreenUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by chenjie07 on 16/12/20.
 * 导航卡片，显示最近一周或一月
 */

public class CustomGraphic extends View {
    public static final int MONTH = 31;
    public static final int WEEK = 7;
    Paint paint;
    Path path;
    Path colorPath;
    ArrayList<NaviModel> totalData = new ArrayList<>();
    ArrayList<NaviModel> data;

    // 图标点集合坐标
    ArrayList<PointF> circlePointFs;
    // 坐标轴数字坐标
    ArrayList<PointF> coordinatePointFs;
    // 平均值线坐标
    ArrayList<PointF> averageLinePoinFs;
    // 平均值文字位置
    PointF averageTextpointF;
    // 文字margin平均线的距离
    int marginAverage = 0;

    float ratio;
    int width = -1;
    int height = -1;
    // 左右边距
    int marginHorizontal = 0;
    // 距离上边边距
    int marginTop = 0;
    // 距离下边边距
    int marginBottom = 0;
    int marginBottomWhite = 0;
    // 32pix字体
    int font32 = 0;
    // 18pix字体
    int font18 = 0;
    // smallCircle半径
    int smallCircleR = 0;
    int middleCircleR = 0;
    int bigCircleR = 0;
    // 折线图高度上下压缩的距离
    int graphicMargin = 0;

    // 开关控件尺寸
    int switchWidth = 0;
    int switchHight = 0;
    int switchMarginLeft = 0;
    int switchCircleR = 0;
    // 不加一些偏移的话，开关的位置会偏上
    int switchVerticalPadding = 0;
    int switchTextPadding = 0;
    PointF switchPointF;
    PointF switchCirclePointLeft;
    PointF switchCirclePointRight;

    // 开关控件path
    Path switchPath;
    Path clickPath;
    Region switchRegion;
    Paint switchPaint;
    Paint switchTextPaint;

    // 详细信息位置1
    int detailInfoBigMarginTop = 0;

    LinearGradient linearGradient;
    LinearGradient backgroundGradiant;
    GestureDetector gestureDetector;

    RectF rectF;
    public int currentNum = MONTH;

    float maxPointY = 0;
    // 绘制文本画笔
    Paint textPaint;

    // 绘制虚线的画笔
    Paint avaeragePaint;

    int currentSelect = currentNum - 1;

    double average;

    // 距离提示文案的 坐标点
    Paint distancePaint;

    // 距离标牌
    int rectMinWidth = 0;
    int rectHeight = 0;
    int rectRoundR = 0;
    int triangleWidth = 0;
    int triangleHeight = 0;
    int marginCirclePoint = 0;
    RectF flagRectF;
    Path distancePath;

    private void setPixs() {
        marginHorizontal = ScreenUtils.dip2px(15, getContext());
        marginTop = ScreenUtils.dip2px(40, getContext());
        marginBottom = ScreenUtils.dip2px(25, getContext());
        marginBottomWhite = ScreenUtils.dip2px(5, getContext());
        marginAverage = ScreenUtils.dip2px(4, getContext());
        font32 = ScreenUtils.dip2px(16, getContext());
        font18 = ScreenUtils.dip2px(9, getContext());
        smallCircleR = ScreenUtils.dip2px(1.5f, getContext());
        middleCircleR = ScreenUtils.dip2px(3, getContext());
        bigCircleR = ScreenUtils.dip2px(5, getContext());
        detailInfoBigMarginTop = ScreenUtils.dip2px(10, getContext());

        switchWidth = ScreenUtils.dip2px(32, getContext());
        switchHight = ScreenUtils.dip2px(17, getContext());
        switchCircleR = ScreenUtils.dip2px(8, getContext());
        switchMarginLeft = ScreenUtils.dip2px(40, getContext());
        switchVerticalPadding = ScreenUtils.dip2px(1, getContext());
        switchTextPadding = ScreenUtils.dip2px(0.4f, getContext());
        graphicMargin = ScreenUtils.dip2px(5, getContext());

        rectMinWidth = ScreenUtils.dip2px(16, getContext());
        rectHeight = ScreenUtils.dip2px(17, getContext());
        rectRoundR = ScreenUtils.dip2px(2, getContext());
        triangleWidth = ScreenUtils.dip2px(4, getContext());
        triangleHeight = ScreenUtils.dip2px(3, getContext());
        marginCirclePoint = ScreenUtils.dip2px(7, getContext());
    }

    public CustomGraphic(Context context) {
        super(context);
        init();
    }

    public CustomGraphic(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomGraphic(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(21)
    public CustomGraphic(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        currentNum = AppConfig.getInstance().isCurrentNaviCardMonth() ? MONTH : WEEK;
        currentSelect = currentNum - 1;
        setPixs();
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);

        distancePaint = new Paint();
        distancePaint.setColor(0xff14acff);
        distancePaint.setAntiAlias(true);
        distancePaint.setTextSize(font18);

        switchPaint = new Paint();
        switchPaint.setColor(0xff14acff);
        switchPaint.setAntiAlias(true);
        switchPaint.setStyle(Paint.Style.FILL);

        switchTextPaint = new Paint();
        switchTextPaint.setColor(0xffffffff);
        switchTextPaint.setTextSize(font18);
        switchTextPaint.setAntiAlias(true);

        textPaint = new Paint();
        textPaint.setColor(0xff8350a9);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setTextSize(font18);
        textPaint.setAntiAlias(true);

        avaeragePaint = new Paint();
        avaeragePaint.setColor(0xffcccccc);
        avaeragePaint.setStyle(Paint.Style.STROKE);
        PathEffect effects = new DashPathEffect(new float[]{10, 5, 10, 5}, 1);
        avaeragePaint.setPathEffect(effects);

        gestureDetector = new GestureDetector(getContext(), new GestureDetector.OnGestureListener() {
            @Override
            public boolean onDown(MotionEvent e) {
                return false;
            }

            @Override
            public void onShowPress(MotionEvent e) {

            }

            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                float x = e.getX();
                float y = e.getY();
                if (x > marginHorizontal && x < width - marginHorizontal
                        && y > marginTop && y < height - marginBottomWhite) {
                    // ControlLogStatistics.getInstance().addLog("PCenterPG.naviChartSelectPoint");
                    float w = (width - 2 * marginHorizontal) * 1.0f / (currentNum - 1);
                    int index = (int) ((x - marginHorizontal + w / 2) / w);
                    currentSelect = index;
                    postInvalidate();
                } else {
                    if (switchRegion.contains((int) x, (int) y)) {
                        // ControlLogStatistics.getInstance().addLog("PCenterPG.naviChartMonthWeek");
                        currentNum = (currentNum == WEEK ? MONTH : WEEK);
                        currentSelect = currentNum - 1;
                        AppConfig.getInstance().setCurrentNaviCardMonth(currentNum == MONTH);
                        if (!totalData.isEmpty()) {
                            updateData(totalData.subList(MONTH - currentNum, MONTH));
                        } else {
                            updateData(totalData);
                        }
                        postInvalidate();
                    }
                }

                return true;
            }

            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                return false;
            }

            @Override
            public void onLongPress(MotionEvent e) {

            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                return false;
            }
        });
        paint = new Paint();
        paint.setColor(0xff14acff);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeCap(Paint.Cap.SQUARE);
        paint.setStrokeJoin(Paint.Join.MITER);
        paint.setAntiAlias(true);


        path = new Path();
        colorPath = new Path();
        data = new ArrayList<>();
        circlePointFs = new ArrayList<>();
        coordinatePointFs = new ArrayList<>();
        averageLinePoinFs = new ArrayList<>();

        flagRectF = new RectF();
        distancePath = new Path();
    }

    private void updateData(List<NaviModel> data) {
        this.data.clear();
        this.data.addAll(data);
        if (width != -1 && height != -1) {
            initXY();
        }
    }

    public void setTotalData(List<NaviModel> data) {
        this.totalData.clear();
        this.totalData.addAll(data);
        currentNum = AppConfig.getInstance().isCurrentNaviCardMonth() ? MONTH : WEEK;
        currentSelect = currentNum - 1;
        if (!totalData.isEmpty()) {
            updateData(totalData.subList(MONTH - currentNum, MONTH));
        } else {
            updateData(totalData);
        }
        postInvalidate();
    }

    private void initXY() {
        path.reset();
        colorPath.reset();
        circlePointFs.clear();
        averageLinePoinFs.clear();
        coordinatePointFs.clear();

        int h = height;
        int w = width;
        if (data != null && data.size() == currentNum) {
            long max = 0;
            for (int i = 0; i < currentNum; i++) {
                if (max < data.get(i).distance) {
                    max = data.get(i).distance;
                }
            }
            maxPointY = max;
            max = getMax(max);
            ratio = (h - marginTop - marginBottom - graphicMargin) * 1.0f / max;
            maxPointY = marginBottom + graphicMargin + ratio * maxPointY;

            colorPath.moveTo(marginHorizontal, marginBottom);
            for (int i = 0; i < currentNum; i++) {
                float x = marginHorizontal + (w - 2 * marginHorizontal) * 1.0f * i / (currentNum - 1);
                float y = marginBottom + graphicMargin + data.get(i).distance * ratio;
                circlePointFs.add(new PointF(x, y));
                colorPath.lineTo(x, y);
                if (i == 0) {
                    path.moveTo(x, y);
                } else {
                    path.lineTo(x, y);
                }
                if (i == currentNum - 1) {
                    colorPath.lineTo(x, marginBottom);
                }
            }
            colorPath.close();

            average = max / 2;

            if (average < 1000) {
                average = (int) average;
            }
            averageLinePoinFs.add(new PointF(marginHorizontal,
                    (float) (marginBottom + graphicMargin + ratio * average)));
            averageLinePoinFs.add(new PointF(w - marginHorizontal,
                    (float) (marginBottom + graphicMargin + ratio * average)));

            averageTextpointF = new PointF(marginHorizontal,
                    (float) (height - (marginBottom + graphicMargin + average * ratio)
                            - font18 / 2 - marginAverage));
        }
        if (maxPointY != 0) {
            linearGradient = new LinearGradient(0, maxPointY, 0, marginBottom,
                    new int[]{0x4d14acff, 0x0014acff}, null, Shader.TileMode.CLAMP);
        } else {
            linearGradient = new LinearGradient(0, h, 0, marginBottom,
                    new int[]{0x4d14acff, 0x0014acff}, null, Shader.TileMode.CLAMP);
        }

        // 计算坐标轴文字点的坐标
        if (currentNum == WEEK) {
            for (int i = 0; i < currentNum; i++) {
                float x = marginHorizontal + (w - 2 * marginHorizontal) * 1.0f * i / (currentNum - 1);
                float y = h - marginBottomWhite - (marginBottom - marginBottomWhite) / 2;
                coordinatePointFs.add(new PointF(x, y));
            }
        } else {
            for (int i = 0; i < currentNum; i = i + 5) {
                float x = marginHorizontal + (w - 2 * marginHorizontal) * 1.0f * i / (currentNum - 1);
                float y = h - marginBottomWhite - (marginBottom - marginBottomWhite) / 2;
                coordinatePointFs.add(new PointF(x, y));
            }
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        switchPointF = new PointF(marginHorizontal + switchMarginLeft,
                (marginTop - switchHight) / 2 + switchVerticalPadding);
        switchCirclePointLeft = new PointF(switchPointF.x + switchHight / 2, switchPointF.y + switchHight / 2);
        switchCirclePointRight = new PointF(switchPointF.x + switchWidth - switchHight / 2,
                switchPointF.y + switchHight / 2);

        switchPath = new Path();
        RectF switchRectF = new RectF(switchPointF.x, switchPointF.y,
                switchPointF.x + switchWidth, switchPointF.y + switchHight);
        switchPath.addRoundRect(switchRectF, switchHight / 2, switchHight / 2, Path.Direction.CW);
        switchPath.close();

        switchRegion = new Region();
        clickPath = new Path();
        // 增大点击区域
        RectF clickRectF = new RectF(switchRectF.left - detailInfoBigMarginTop,
                switchRectF.top - detailInfoBigMarginTop,
                switchRectF.right + detailInfoBigMarginTop,
                switchRectF.bottom + detailInfoBigMarginTop);
        clickPath.addRect(clickRectF, Path.Direction.CW);
        clickPath.close();
        switchRegion.setPath(clickPath, new Region(0, 0, w, h));

        width = w;
        height = h;
        rectF = new RectF(0, 0, width, height);
        backgroundGradiant = new LinearGradient(0, h, 0, 0,
                new int[]{0xffffffff, 0xffffffff, 0xffffffff}, null, Shader.TileMode.REPEAT);

        initXY();
    }

    private long getMax(long currentMax) {
        if (currentMax < 1000L) {
            return (currentMax / 10L + 1) * 10L;
        } else if (currentMax < 100000L) {
            // 向上取偶数公里
            long kilo = currentMax / 1000L + 1;
            return kilo / 2 == 0 ? kilo * 1000L : (kilo + 1) * 1000L;
        } else {
            return (currentMax / 10000L + 1) * 10000L;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        paint.setColor(0xFFFFFFFF);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawRect(rectF, paint);
        canvas.save();
        canvas.scale(1, -1);
        canvas.translate(0, -height);
        paint.setShader(null);
        paint.setStyle(Paint.Style.STROKE);
        paint.setShader(null);
        paint.setColor(0xaaaaaaaa);
        // 坐标轴
        paint.setStrokeWidth(1);
        canvas.drawLine(marginHorizontal, marginBottom, width - marginHorizontal, marginBottom, paint);
        // 平均值线
        if (averageLinePoinFs.size() > 0) {
            canvas.drawLine(averageLinePoinFs.get(0).x, averageLinePoinFs.get(0).y,
                    averageLinePoinFs.get(1).x, averageLinePoinFs.get(1).y, avaeragePaint);
        }

        // 画曲线图
        paint.setColor(0xff14acff);
        paint.setStrokeWidth(2);
        if (!path.isEmpty()) {
            canvas.drawPath(path, paint);
        }

        // 画曲面图
        paint.setStyle(Paint.Style.FILL);
        paint.setShader(linearGradient);
        if (!colorPath.isEmpty()) {
            canvas.drawPath(colorPath, paint);
        }
        // 画圆点
        paint.setShader(null);
        for (int i = 0; i < circlePointFs.size(); i++) {
            PointF pointF = circlePointFs.get(i);
            if (i == currentSelect) {
                paint.setColor(0xff14acff);
                canvas.drawCircle(pointF.x, pointF.y, middleCircleR, paint);
                paint.setColor(0x7014acff);
                canvas.drawCircle(pointF.x, pointF.y, bigCircleR, paint);
            } else {
                paint.setColor(0xff14acff);
                canvas.drawCircle(pointF.x, pointF.y, smallCircleR, paint);
            }
        }
        canvas.restore();

        // 画坐标轴刻度
        for (int i = 0; i < coordinatePointFs.size(); i++) {
            PointF pointF = coordinatePointFs.get(i);
            long time = 0;
            if (!data.isEmpty()) {
                if (currentNum == WEEK) {
                    time = data.get(i).time;
                } else {
                    time = data.get(5 * i).time;
                }

            } else {
                if (currentNum == WEEK) {
                    time = System.currentTimeMillis() - (currentNum - 1 - i) * 24 * 3600 * 1000L;
                } else {
                    time = System.currentTimeMillis() - (currentNum - 1 - 5 * i) * 24 * 3600 * 1000L;
                }
            }
            int select = i;
            if (currentNum == MONTH) {
                select = 5 * i;
            }
            if (select != currentSelect) {
                textPaint.setTextSize(font18);
                textPaint.setColor(0xff999999);
            } else {
                textPaint.setTextSize(font18);
                textPaint.setColor(0xff14ACFF);
            }
            if (i == 0) {
                drawText(canvas, getFirst(time), pointF.x, pointF.y, textPaint, Paint.Align.LEFT);
            } else if (i == coordinatePointFs.size() - 1) {
                int day = getDaybyTimeStamp(time);
                drawText(canvas, day + "", pointF.x, pointF.y, textPaint, Paint.Align.RIGHT);
            } else {
                int day = getDaybyTimeStamp(time);
                drawText(canvas, day + "", pointF.x, pointF.y, textPaint, Paint.Align.CENTER);
            }
        }

        // 导航文案
        textPaint.setTextSize(font32);
        textPaint.setColor(0xff333333);
        textPaint.setFakeBoldText(false);
        drawText(canvas, "导航", marginHorizontal, marginTop / 2, textPaint, Paint.Align.LEFT);

        // 平均值信息
        if (!data.isEmpty()) {
            textPaint.setTextSize(font18);
            textPaint.setColor(0xff999999);
            if (average < 1000) {
                drawText(canvas, (int) average + "m", averageTextpointF.x, averageTextpointF.y,
                        textPaint, Paint.Align.LEFT);
            } else {
                drawText(canvas, (int) (average / 1000) + "km",
                        averageTextpointF.x, averageTextpointF.y, textPaint, Paint.Align.LEFT);
            }
        }

        // 画开关按钮
        switchPaint.setColor(0xff14acff);
        canvas.drawPath(switchPath, switchPaint);
        switchPaint.setColor(0xffffffff);
        if (currentNum == MONTH) {
            drawText(canvas, "月", switchCirclePointLeft.x + smallCircleR, switchCirclePointLeft.y - switchTextPadding,
                    switchTextPaint, Paint.Align.CENTER);
            canvas.drawCircle(switchCirclePointRight.x, switchCirclePointRight.y, switchCircleR, switchPaint);
        } else {
            drawText(canvas, "周", switchCirclePointRight.x - smallCircleR,
                    switchCirclePointRight.y - switchTextPadding, switchTextPaint, Paint.Align.CENTER);
            canvas.drawCircle(switchCirclePointLeft.x, switchCirclePointLeft.y, switchCircleR, switchPaint);
        }

        if (!data.isEmpty()) {

            // 获取当前点的坐标及距离信息
            PointF pointF = circlePointFs.get(currentSelect);
            NaviModel model = data.get(currentSelect);

            // 计算矩形标牌的宽度
            String str;
            if (model.distance < 1000) {
                str = model.distance + "m";
            } else {
                str = String.format("%.1f", model.distance / 1000f) + "km";
            }
            int textWidth = getTextWidth(distancePaint, str);

            // 决定当前距离标盘显示的样式, 计算标牌的坐标值  if (model.distance > average) {
            if (pointF.y + rectHeight + triangleHeight + marginCirclePoint > height
                    - marginTop + detailInfoBigMarginTop) {
                if (pointF.x > width / 2) {
                    // 左下方
                    float right = pointF.x;
                    float bottom = height - pointF.y + marginCirclePoint + triangleHeight + rectHeight;
                    float left = right - textWidth - rectMinWidth;
                    float top = bottom - rectHeight;
                    flagRectF.set(left, top, right, bottom);
                    distancePath.reset();
                    distancePath.moveTo(right, top - triangleHeight);
                    distancePath.lineTo(right, top + rectRoundR);
                    distancePath.lineTo(right - triangleWidth, top);
                    distancePath.close();
                } else {
                    // 右下方
                    float left = pointF.x;
                    float top = height - pointF.y + marginCirclePoint + triangleHeight;
                    float right = left + textWidth + rectMinWidth;
                    float bottom = top + rectHeight;
                    flagRectF.set(left, top, right, bottom);
                    distancePath.reset();
                    distancePath.moveTo(left, top - triangleHeight);
                    distancePath.lineTo(left, top + rectRoundR);
                    distancePath.lineTo(left + triangleWidth, top);
                    distancePath.close();
                }
            } else {
                if (pointF.x > width / 2) {
                    // 左上方
                    float right = pointF.x;
                    float bottom = height - pointF.y - marginCirclePoint - triangleHeight;
                    float left = right - textWidth - rectMinWidth;
                    float top = bottom - rectHeight;
                    flagRectF.set(left, top, right, bottom);
                    distancePath.reset();
                    distancePath.moveTo(right, bottom + triangleHeight);
                    distancePath.lineTo(right, bottom - rectRoundR);
                    distancePath.lineTo(right - triangleWidth, bottom);
                    distancePath.close();
                } else {
                    // 右上方
                    float left = pointF.x;
                    float top = height - pointF.y - marginCirclePoint - triangleHeight - rectHeight;
                    float right = left + textWidth + rectMinWidth;
                    float bottom = top + rectHeight;
                    flagRectF.set(left, top, right, bottom);
                    distancePath.reset();
                    distancePath.moveTo(left, top + rectHeight + triangleHeight);
                    distancePath.lineTo(left, top + rectHeight - rectRoundR);
                    distancePath.lineTo(left + triangleWidth, bottom);
                    distancePath.close();
                }
            }
            // 画标牌
            canvas.drawRoundRect(flagRectF, rectRoundR, rectRoundR, distancePaint);
            canvas.drawPath(distancePath, distancePaint);
            // 画文案
            textPaint.setColor(0xffffffff);
            textPaint.setTextSize(font18);
            textPaint.setFakeBoldText(true);
            drawText(canvas, str, flagRectF.centerX(), flagRectF.centerY(), textPaint, Paint.Align.CENTER);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        gestureDetector.onTouchEvent(event);
        return true;
    }

    /**
     * 以centerX,centerY为参照点绘制文本，可以指定左，中，右
     *
     * @param canvas
     * @param str
     * @param centerX
     * @param centerY
     * @param paint
     */
    private void drawText(Canvas canvas, String str, float centerX, float centerY, Paint paint, Paint.Align align) {
        paint.setTextAlign(align);
        Paint.FontMetrics fontMetrics = paint.getFontMetrics();
        canvas.drawText(str, centerX, centerY - fontMetrics.bottom + (fontMetrics.bottom - fontMetrics.top) / 2, paint);
    }

    private String timeStampToDate(long timeStamp) {
        Date date = new Date(timeStamp);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM.dd");
        String dateStr = simpleDateFormat.format(date);
        return dateStr;
    }

    private int getDaybyTimeStamp(long timeStamp) {
        String date = timeStampToDate(timeStamp);
        String day = date.substring(3, 5);
        return Integer.parseInt(day);
    }

    private int getMonthbyTimeStamp(long timeStamp) {
        String date = timeStampToDate(timeStamp);
        String month = date.substring(0, 2);
        return Integer.parseInt(month);
    }

    private String getFirst(long timeStamp) {
        return getMonthbyTimeStamp(timeStamp) + "月" + getDaybyTimeStamp(timeStamp);
    }

    private int getTextWidth(Paint paint, String str) {
        Rect rect = new Rect();
        paint.getTextBounds(str, 0, str.length(), rect);
        return rect.width();
    }
}
