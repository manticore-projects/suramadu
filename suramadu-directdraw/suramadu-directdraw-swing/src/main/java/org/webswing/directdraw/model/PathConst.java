package org.webswing.directdraw.model;

import org.webswing.directdraw.DirectDraw;
import org.webswing.directdraw.proto.Directdraw.PathProto;
import org.webswing.directdraw.proto.Directdraw.PathProto.SegmentTypeProto;

import java.awt.Shape;
import java.awt.geom.Path2D;
import java.awt.geom.PathIterator;
import java.util.List;

public class PathConst extends MutableDrawConstantHolder<Shape, PathProto> {

  public PathConst(DirectDraw context, Shape value) {
    super(context, value);
  }

  @Override
  public String getFieldName() {
    return "path";
  }

  @Override
  public PathProto buildMessage(Shape value) {
    PathProto.Builder model = PathProto.newBuilder();
    PathIterator iterator = value.getPathIterator(null);
    if (iterator != null) {
      model.setWindingOdd(iterator.getWindingRule() == PathIterator.WIND_EVEN_ODD);
      double[] points = new double[6];
      while (!iterator.isDone()) {
        int type = iterator.currentSegment(points);
        int pointCount = getPointCount(type);
        model.addType(SegmentTypeProto.valueOf(type));
        for (int i = 0; i < pointCount; i++) {
          model.addPoints((float) points[i]);
        }
        iterator.next();
      }
    }
    return model.build();
  }

  private int getPointCount(int segmentType) {
    switch (segmentType) {
      case PathIterator.SEG_MOVETO:
      case PathIterator.SEG_LINETO:
        return 2;
      case PathIterator.SEG_QUADTO:
        return 4;
      case PathIterator.SEG_CUBICTO:
        return 6;
      case PathIterator.SEG_CLOSE:
      default:
        return 0;
    }
  }

  @Override
  public Shape getValue() {
    Path2D.Float path = new Path2D.Float(
        message.getWindingOdd() ? PathIterator.WIND_EVEN_ODD : PathIterator.WIND_NON_ZERO);
    List<Float> pts = message.getPointsList();
    int offset = 0;
    for (SegmentTypeProto type : message.getTypeList()) {
      int pointCount = type == SegmentTypeProto.CLOSE ? 0
          : type == SegmentTypeProto.MOVE || type == SegmentTypeProto.LINE ? 2
              : type == SegmentTypeProto.QUAD ? 4 : type == SegmentTypeProto.CUBIC ? 6 : 0;
      switch (type) {
        case MOVE:
          path.moveTo(pts.get(offset), pts.get(offset + 1));
          break;
        case LINE:
          path.lineTo(pts.get(offset), pts.get(offset + 1));
          break;
        case QUAD:
          path.quadTo(pts.get(offset), pts.get(offset + 1), pts.get(offset + 2),
              pts.get(offset + 3));
          break;
        case CUBIC:
          path.curveTo(pts.get(offset), pts.get(offset + 1), pts.get(offset + 2),
              pts.get(offset + 3), pts.get(offset + 4), pts.get(offset + 5));
          break;
        case CLOSE:
          path.closePath();
          break;
      }
      offset += pointCount;
    }
    return path;
  }
}
