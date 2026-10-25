package org.webswing.directdraw.util;

import org.webswing.directdraw.DirectDraw;
import org.webswing.directdraw.model.DrawConstant;
import org.webswing.directdraw.model.FontFaceConst;
import org.webswing.directdraw.model.ImageConst;
import org.webswing.directdraw.proto.Directdraw.DrawConstantProto;

import java.util.*;

public class DrawConstantPool {

  private static final int IMG_CACHE_SIZE = Integer.getInteger("webswing.ddImageCacheSize", 128);
  private static final int CONSTANT_CACHE_SIZE =
      Integer.getInteger("webswing.ddConstCacheSize", 8192);
  public static final int CONSTANT_CACHE_SIZE_MAX =
      Integer.getInteger("webswing.ddMaxConstCacheSize", 8192 * 32);

  private final LRUDrawConstantPoolCache pool;
  private final LRUDrawConstantPoolCache imgPool;
  private final Set<String> registeredFonts = new HashSet<String>();
  private final Map<String, FontFaceConst> requestedFonts = new HashMap<String, FontFaceConst>();
  /**
   * Nesting depth of WebImage encoding. A top-level window frame may contain nested WebImages
   * (DRAW_WEBIMAGE, e.g. Swing's back buffer blitted into the window), which are encoded
   * recursively into the same pool while the outer frame is being built.
   */
  private int encodeDepth;

  public DrawConstantPool() {
    pool = new LRUDrawConstantPoolCache(CONSTANT_CACHE_SIZE, 0, CONSTANT_CACHE_SIZE_MAX);
    imgPool = new LRUDrawConstantPoolCache(IMG_CACHE_SIZE, CONSTANT_CACHE_SIZE_MAX,
        CONSTANT_CACHE_SIZE_MAX);
  }

  /**
   * Marks the start of encoding one WebImage.
   *
   * @return true for the outermost (top-level frame) encode, false for a nested image
   */
  public boolean enterEncode() {
    return encodeDepth++ == 0;
  }

  public void exitEncode() {
    encodeDepth--;
  }

  /**
   * Starts a new frame for the eviction guard. Must be called once per TOP-LEVEL frame only: the
   * caches never evict an entry used in the current frame (they grow instead), so no id is recycled
   * within one frame. (Formerly a call counter, which also counted cache hits and so hit the hard
   * limit on large frames full of repeated constants.) The client applies a frame's outer constants
   * up front but nested-image constants lazily, i.e. in a different order than the server allocated
   * them, so an id recycled within one frame resolves to the wrong constant on the client.
   * Resetting the counters for every nested image disabled the guard for frames with many nested
   * images (slow clients accumulate hundreds of back-buffer blits per frame) - the cause of the
   * DirectDraw corruption on slow connections.
   */
  public void resetCacheOverflowCounters() {
    pool.startFrame();
    imgPool.startFrame();
  }

  public int addToCache(List<DrawConstantProto> protos, DrawConstant<?> cons) {
    if (cons instanceof ImageConst) {
      return addToCache(imgPool, protos, cons);
    } else {
      return addToCache(pool, protos, cons);
    }
  }

  private int addToCache(LRUDrawConstantPoolCache cache, List<DrawConstantProto> protos,
      DrawConstant<?> cons) {
    // capacity growth is decided by the cache itself (never evicts an entry used in this frame)
    int thisId;
    if (!cache.contains(cons)) {
      DrawConstant<?> cacheEntry = cons.toCacheEntry();
      cache.getOrAdd(cacheEntry);
      DrawConstantProto.Builder proto = DrawConstantProto.newBuilder();
      if (cons.getFieldName() != null) {
        proto.setField(
            DrawConstantProto.Builder.getDescriptor().findFieldByName(cons.getFieldName()),
            cons.toMessage());
      }
      proto.setId(cacheEntry.getId());
      protos.add(proto.build());
      thisId = cacheEntry.getId();
    } else {
      thisId = cache.getOrAdd(cons).getId();
    }
    return thisId;
  }

  public synchronized boolean isFontRegistered(String file) {
    if (requestedFonts.containsKey(file) || registeredFonts.contains(file)
        || Boolean.getBoolean(DirectDraw.FONTS_PROVIDED)) {
      return true;
    }
    return false;
  }

  public synchronized void requestFont(String file, FontFaceConst fontFaceConst) {
    requestedFonts.put(file, fontFaceConst);
  }

  public synchronized Collection<FontFaceConst> registerRequestedFonts() {
    if (requestedFonts.size() > 0) {
      Collection<FontFaceConst> result = new ArrayList<FontFaceConst>(requestedFonts.values());
      registeredFonts.addAll(requestedFonts.keySet());
      requestedFonts.clear();
      return result;
    }
    return Collections.emptyList();
  }

}
