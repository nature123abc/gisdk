package com.dk.microgis.line.curve.gradient;



/**
 * @author hq
 * @date 2021-04-28 18:15
 * @desc 完整曲线要素
 */
public class CommonGradient extends BaseGradient {
    public Double slopeAng;//坡度 ‰
   // public Double slopeLen;//坡长
    public Double startDk;//
    public Double endDk;//
    public Double gradientLen;//曲线长

    /**
     * 竖曲线转角(弧度）
     */
    public Double afa;
    /**
     * 竖曲线的切线长度
     */
    public Double tangentLen;
    /**
     * 竖曲线的外矢距
     */
    public Double extDis;
    /**
     * 竖曲线方向(凸形，凹形)
     */
    public GradientType graCurvePX;


    public CommonGradient(CommonGradient gradient) {
        super(gradient);
        this.slopeAng = gradient.slopeAng;
        //this.slopeLen = gradient.slopeLen;
        this.startDk = gradient.startDk;
        this.endDk = gradient.endDk;
        this.afa = gradient.afa;
        this.tangentLen = gradient.tangentLen;
        this.extDis = gradient.extDis;
        this.graCurvePX = gradient.graCurvePX;
    }

    public CommonGradient(BaseGradient gradient) {
        super(gradient);
    }

    public Double getSlopeAng() {
        return slopeAng;
    }

    public void setSlopeAng(Double slopeAng) {
        this.slopeAng = slopeAng;
    }

    public Double getStartDk() {
        return startDk;
    }

    public void setStartDk(Double startDk) {
        this.startDk = startDk;
    }

    public Double getEndDk() {
        return endDk;
    }

    public void setEndDk(Double endDk) {
        this.endDk = endDk;
    }

    public Double getGradientLen() {
        return gradientLen;
    }

    public void setGradientLen(Double gradientLen) {
        this.gradientLen = gradientLen;
    }

    public Double getAfa() {
        return afa;
    }

    public void setAfa(Double afa) {
        this.afa = afa;
    }

    public Double getTangentLen() {
        return tangentLen;
    }

    public void setTangentLen(Double tangentLen) {
        this.tangentLen = tangentLen;
    }

    public Double getExtDis() {
        return extDis;
    }

    public void setExtDis(Double extDis) {
        this.extDis = extDis;
    }

    public GradientType getGraCurvePX() {
        return graCurvePX;
    }

    public void setGraCurvePX(GradientType graCurvePX) {
        this.graCurvePX = graCurvePX;
    }
}
