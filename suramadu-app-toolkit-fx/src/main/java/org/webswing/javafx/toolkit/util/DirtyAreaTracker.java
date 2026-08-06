package org.webswing.javafx.toolkit.util;

import com.sun.javafx.geom.RectBounds;
import com.sun.prism.web.WebTextureWrapper;

import java.nio.Buffer;
import java.util.*;

public class DirtyAreaTracker {
  public final Map<Buffer, Set<RectBounds>> areasByBufferLookup =
      Collections.synchronizedMap(new WeakHashMap<>());
  public final Map<WebTextureWrapper, Set<RectBounds>> areasByTextureLookup =
      Collections.synchronizedMap(new WeakHashMap<>());

  public void registerTextureForPixelBuffer(WebTextureWrapper texture, Buffer buffer) {
    Set<RectBounds> areas = Collections.synchronizedSet(new HashSet<>());
    areasByBufferLookup.put(buffer, areas);
    areasByTextureLookup.put(texture, areas);
  }

  public void addDirtyArea(WebTextureWrapper texture, RectBounds area) {
    Set<RectBounds> areas = areasByTextureLookup.get(texture);
    if (areas != null) {
      areas.add(area);
    }
  }

  public Set<RectBounds> popDirtyAreasForBuffer(Buffer buffer) {
    Set<RectBounds> areas = areasByBufferLookup.remove(buffer);
    Set<RectBounds> result = null;
    if (areas != null) {
      result = new HashSet<>(areas);
      areas.clear();
    }
    return result;
  }
}
