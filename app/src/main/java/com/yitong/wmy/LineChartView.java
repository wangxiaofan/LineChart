package com.yitong.wmy;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

import java.text.DecimalFormat;

/**
 * Created by wangmingyun on 2016/6/2.
 */
public class LineChartView extends View {
    private static float tbPadding = 5f;
    private static float startX = 0f;
    private static float startY = 0f;
    private static float endX = 0f;
    private static float endY = 0f;
    private static float tiblewidth = 0f;
    private static float tibleheight = 0f;
    private static float imgWidth = 0f;
    private static float imgHeight = 0f;
    public static int screenWidth;
    private float radio;// 不同分辨率的 比率
    private float textSize;// 字体大小
    private float circleSize;// 最后一天收益字体大小
    private Context act;
    ViewTreeObserver vto;
    ImageView llBitmap;
    // 数据的和，平均值，Y轴最大值
    float maxYValue, minYValue, pjvalue;
    Bitmap bit;
    // Y轴坐标显示
    private String[] date = new String[]{};
    private Float[] lilv = new Float[]{};
    Paint paint, paint2, paint3, paint4, paint5, paint6, paint7, paint8;
    private int ttWight;
    private int ttheight = 5;
    private float lineToLeft = 0;// 横线居左边距离
    private DecimalFormat fmt = new DecimalFormat("##0.0");
    private String[] yStr;
    private String lineColor;
    public static final String BLUE = "blue";
    public static final String YELLOW = "yellow";
    private Shader mShader;// 设置渐变背景色

    public LineChartView(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
    }

    public LineChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
    }

    public LineChartView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        // TODO Auto-generated constructor stub
    }

    public void setData(Context act, String[] date, Float[] lilv,
                        String lineColor) {
        this.lilv = lilv;
        this.date = date;
        this.act = act;
        this.lineColor = lineColor;
        init();
        invalidate();
    }

    private void init() {
        ttWight = date.length - 1;
        screenWidth = AndroidUtil.getScreenWidth(act);
        radio = screenWidth / 320;
        textSize = 10 * radio;
        circleSize = 12 * radio;

        // 绘制表格的线
        paint = new Paint();
        paint.setColor(Color.rgb(203, 203, 203));// 可以直接设置颜色，也可通过Argb方法设置精确颜色
        paint.setAntiAlias(true);
        paint.setStrokeWidth(AndroidUtil.dip2px(act, (float) 0.1));

        // 绘制字体
        paint2 = new Paint();
        paint2.setColor(Color.rgb(173, 173, 173));
        paint2.setStrokeWidth(AndroidUtil.dip2px(act, (float) 0.2));
        paint2.setAntiAlias(true);
        paint2.setTextSize(textSize);

        // 绘制折线
        paint3 = new Paint();
        if (lineColor.equals(BLUE)) {
            paint3.setColor(Color.parseColor("#87CAEA"));
        } else {
            paint3.setColor(Color.parseColor("#F9BF79"));
        }
        paint3.setAntiAlias(true);
        paint3.setStyle(Paint.Style.FILL);
        paint3.setStrokeWidth(AndroidUtil.dip2px(act, (float) 3));

        // 绘制圆圈
        paint4 = new Paint();
        if (lineColor.equals(BLUE)) {
            paint4.setColor(Color.parseColor("#87CAEA"));
        } else {
            paint4.setColor(Color.parseColor("#F9BF79"));
        }
        paint4.setAntiAlias(true);

        // 绘制白色圆圈
        paint5 = new Paint();
        paint5.setColor(Color.WHITE);
        paint5.setAntiAlias(true);

        // 绘制标注的图形
        paint6 = new Paint();
        paint6.setStyle(Paint.Style.FILL);// 充满
        if (lineColor.equals(BLUE)) {
            paint6.setColor(Color.parseColor("#87CAEA"));
        } else {
            paint6.setColor(Color.parseColor("#F9BF79"));
        }
        paint6.setAntiAlias(true);// 设置画笔的锯齿效果

        // 绘制最后的字体
        paint7 = new Paint();
        paint7.setColor(Color.rgb(255, 255, 255));
        paint7.setAntiAlias(true);
        paint7.setTextAlign(Paint.Align.CENTER);
        paint7.setTextSize(circleSize);

        // 折线图包围起来的区域背景色
        paint8 = new Paint();
        paint8.setAntiAlias(true);
        paint8.setStyle(Paint.Style.FILL);// 充满

        lineToLeft = paint2.measureText("0.0");
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // TODO Auto-generated method stub
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // TODO Auto-generated method stub
        super.onDraw(canvas);
        imgWidth = getWidth();
        imgHeight = getHeight();

        initSize();
        initYValue(lilv);

        // 绘制横线 Y轴坐标
        for (int i = 0; i < ttheight + 1; i++) {
            // 画线和数字
            canvas.drawLine(startX + lineToLeft + 0.1f * tiblewidth / ttWight,
                    startY + i * tibleheight / ttheight, endX, startY + i
                            * tibleheight / ttheight, paint);
            paint2.setTextAlign(Paint.Align.LEFT);
            canvas.drawText(yStr[i], startX + 0.1f * tiblewidth / ttWight,
                    startY + i * tibleheight / ttheight + textSize / 3, paint2);

        }

        // 绘制竖线 X轴坐标
        for (int i = 0; i < date.length; i++) {
            canvas.drawLine(startX + i * tiblewidth / ttWight, startY, startX
                    + i * tiblewidth / ttWight, endY - textSize * 3 / 2, paint);
            if (i == 0) {
                paint2.setTextAlign(Paint.Align.LEFT);
            } else {
                paint2.setTextAlign(Paint.Align.CENTER);
            }
            canvas.drawText(date[i], startX + i * tiblewidth / ttWight, endY,
                    paint2);
        }

        // 绘制折线图渐变背景色
        Path my_path = new Path();
        my_path.moveTo(startX, endY - textSize * 3 / 2);

        // 绘制折线和标注
        for (int i = 0; i < ttWight; i++) {
            float startHY = (tibleheight / 5) * (lilv[i] - minYValue) / pjvalue;
            float endHY = (tibleheight / 5) * (lilv[i + 1] - minYValue)
                    / pjvalue;
            // 画折线图
            canvas.drawLine(startX + i * tiblewidth / ttWight, endY - textSize
                            * 3 / 2 - startHY, startX + (i + 1) * tiblewidth / ttWight,
                    endY - textSize * 3 / 2 - endHY, paint3);

            // 绘制被折线图包围起来的矩形区域
            my_path.lineTo(startX + i * tiblewidth / ttWight, endY - startHY
                    - textSize * 3 / 2);

            if (i == ttWight - 1) {
                // 绘制被折线图包围起来的矩形区域
                my_path.lineTo(startX + (i + 1) * tiblewidth / ttWight, endY
                        - endHY - textSize * 3 / 2);
                my_path.lineTo(startX + ttWight * tiblewidth / ttWight, endY
                        - textSize * 3 / 2);
                my_path.close();
                if (lineColor.equals(BLUE)) {
                    mShader = new LinearGradient(startX, endY, startX + (i + 1)
                            * tiblewidth / ttWight, endY - endHY - textSize * 3
                            / 2, new int[]{Color.parseColor("#0087CAEA"),
                            Color.parseColor("#5087CAEA")}, null,
                            Shader.TileMode.REPEAT);
                } else {
                    mShader = new LinearGradient(startX, endY, startX + (i + 1)
                            * tiblewidth / ttWight, endY - endHY - textSize * 3
                            / 2, new int[]{Color.parseColor("#00F9BF79"),
                            Color.parseColor("#50F9BF79")}, null,
                            Shader.TileMode.REPEAT);
                }
                paint8.setShader(mShader);
                canvas.drawPath(my_path, paint8);

                // 圆圈
                canvas.drawCircle(startX + (i + 1) * tiblewidth / ttWight, endY
                                - textSize * 3 / 2 - endHY,
                        AndroidUtil.dip2px(act, (float) 6), paint4);
                canvas.drawCircle(startX + (i + 1) * tiblewidth / ttWight, endY
                                - textSize * 3 / 2 - endHY,
                        AndroidUtil.dip2px(act, (float) 4), paint5);

                // 绘制矩形
                RectF oval3 = new RectF(startX + (i + 0.5f) * tiblewidth
                        / ttWight,
                        endY - endHY - 1.5f * tibleheight / ttheight, startX
                        + (i + 1.5f) * tiblewidth / ttWight, endY
                        - endHY - 0.8f * tibleheight / ttheight);// 设置个新的长方形
                canvas.drawRoundRect(oval3, 10, 5, paint6);

                // 绘制三角形
                Path path = new Path();
                path.moveTo(startX + (i + 1 - 1 / 10f) * tiblewidth / ttWight,
                        endY - endHY - 0.8f * tibleheight / ttheight);// 此点为多边形的起点
                path.lineTo(startX + (i + 1 + 1 / 10f) * tiblewidth / ttWight,
                        endY - endHY - 0.8f * tibleheight / ttheight);
                path.lineTo(startX + (i + 1) * tiblewidth / ttWight, endY
                        - endHY - textSize * 3 / 2 - 0.1f * tiblewidth
                        / ttWight);
                path.close(); // 使这些点构成封闭的多边形
                canvas.drawPath(path, paint6);

                // 写入最后一天的利润
                String strLast = lilv[lilv.length - 1].toString();
                int l = strLast.length();
                if (l < 6) {
                    for (int k = 0; k < 6 - l; k++) {
                        strLast = strLast + "0";
                    }
                }
                canvas.drawText(strLast + "", startX + (i + 1) * tiblewidth
                                / ttWight,
                        endY - endHY - 1.0f * tibleheight / ttheight, paint7);
            }
        }
    }

    private void initSize() {
        // 初始化画布宽高和各个起始点位置
        startX = AndroidUtil.dip2px(act, (float) tbPadding);
        startY = AndroidUtil.dip2px(act, (float) tbPadding);
        endX = imgWidth - AndroidUtil.dip2px(act, (float) tbPadding);
        endY = imgHeight - AndroidUtil.dip2px(act, (float) tbPadding);
        tiblewidth = (imgWidth - AndroidUtil.dip2px(act, (float) tbPadding) * 2) * 12 / 13;
        tibleheight = imgHeight - AndroidUtil.dip2px(act, (float) tbPadding)
                * 2 - textSize * 3 / 2;
    }

    private void initYValue(Float[] yData) {
        // 先准备好需要的数据
        // 7日利润集为例
        maxYValue = yData[0];
        minYValue = yData[0];
        for (Float f : yData) {
            maxYValue = maxYValue >= f ? maxYValue : f;
            minYValue = minYValue <= f ? minYValue : f;
        }

        maxYValue = (int) maxYValue + 1;
        minYValue = (int) minYValue;
        pjvalue = (maxYValue - minYValue) / 5;
        yStr = new String[yData.length];
        for (int a = 0; a < yData.length; a++) {
            yStr[a] = fmt.format(maxYValue - a * pjvalue);
        }
    }
}
