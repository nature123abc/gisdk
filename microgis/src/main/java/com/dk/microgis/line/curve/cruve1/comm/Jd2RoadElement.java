package com.dk.microgis.line.curve.cruve1.comm;


import com.dk.microgis.error.CommonException;
import com.dk.microgis.line.curve.cruve1.entity.BaseJdPoint;
import com.dk.microgis.line.curve.cruve1.entity.CommonJdPoint;
import com.dk.microgis.line.curve.cruve1.entity.RoadElement;
import com.dk.microgis.line.curve.curve.common.entity.PointType;

import java.util.ArrayList;
import java.util.List;

/**
 * @author hq
 * @date 2021-10-29 16:55
 * @desc 交点计算到曲线元
 */
public class Jd2RoadElement<T extends BaseJdPoint> {

    //计算完成后的线路曲线要素信息（包括交点和起终点）,如果有1个焦点，则有三个值返回，包括起终点
    public List<CommonJdPoint> commonJdPoints;
    List<RoadElement> elements;//线路曲线要素 计算到交点信息
    List<BaseJdPoint> baseJdPoints;//交点格式化后
    List<T> jdOrgPoints;//交点数据


    public Jd2RoadElement(List<T> jdPoints) {
        this.jdOrgPoints = jdPoints;
        init1();
    }

    private void init1() {
        baseJdPoints = new ArrayList<>();
        elements = new ArrayList<>();
        commonJdPoints = new ArrayList<>();

        for (int i = 0; i < jdOrgPoints.size(); i++) {
            T bj = jdOrgPoints.get(i);

            // T r = (T) BeanUtil.copyProperties(bj, bj.getClass());
            BaseJdPoint r = new BaseJdPoint(bj);
            r.pointType = PointType.交点;
            if (i == 0) {
                r.pointType = PointType.起点;
            }
            if (i == jdOrgPoints.size() - 1) {
                r.pointType = PointType.终点;
            }
            r.name = bj.name;

            r.dk = bj.dk;
            baseJdPoints.add(r);
        }
        if (baseJdPoints.size() < 2) {
            throw new CommonException("曲线要素不能少于2个");
        }


        if (baseJdPoints.size() == 2) {
            RoadElement element = new RoadElement(baseJdPoints.get(0), baseJdPoints.get(1));
            element.comput();
            elements.add(element);

            RoadElement element0 = setPintDk(element.startP);
            element0.startP = element.startP;
            element0.endP = element.endP;


            RoadElement element1 = setPintDk(element.endP);
            element1.startP = element.endP;
            element1.endP = element.endP;

            commonJdPoints.add(new CommonJdPoint(jdOrgPoints.get(0), element0));
            commonJdPoints.add(new CommonJdPoint(jdOrgPoints.get(1), element1));
            return;
        }

        for (int i = 1; i < baseJdPoints.size() - 1; i++) {
            BaseJdPoint start = baseJdPoints.get(i - 1);
            BaseJdPoint jd = baseJdPoints.get(i);
            BaseJdPoint end = baseJdPoints.get(i + 1);
            if (i == 1) {
                RoadElement element = new RoadElement(start, jd, end);
                element.comput();
                elements.add(element);
                continue;
            }
            BaseJdPoint start1 = new BaseJdPoint(elements.get(elements.size() - 1).endP);
            RoadElement element = new RoadElement(start1, jd, end);
            element.comput();
            elements.add(element);
        }

        //重新组织完整曲线要素，准备输出
        for (int i = 0; i < jdOrgPoints.size(); i++) {
            T bj = jdOrgPoints.get(i);
            if (i > 0 && i < jdOrgPoints.size() - 1) {
                commonJdPoints.add(new CommonJdPoint(bj, elements.get(i - 1)));
            } else if (i == 0) {//起点
                RoadElement element = setPintDk(elements.get(0).startP);
                element.startP = elements.get(0).startP;
                element.endP = elements.get(0).startP;
                commonJdPoints.add(new CommonJdPoint(bj, element));
            } else {//终点
                RoadElement element = setPintDk(elements.get(elements.size() - 1).endP);
                element.startP = elements.get(elements.size() - 1).endP;
                element.endP = elements.get(elements.size() - 1).endP;
                commonJdPoints.add(new CommonJdPoint(bj, element));
            }
        }
    }

    private RoadElement setPintDk(BaseJdPoint dk) {
        RoadElement element = new RoadElement();
        element.zh = new BaseJdPoint(dk);
        element.hz = new BaseJdPoint(dk);
        return element;
    }
}
