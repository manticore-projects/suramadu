package org.webswing.directdraw.util;

import org.webswing.directdraw.model.DrawConstant;

import java.util.HashMap;

/**
 * LRU cache for {@link DrawConstant} entries. NOT thread-safe by design: all access goes through
 * {@code DrawConstantPool.addToCache}, which is only ever called from
 * {@code WebImage.toMessageInternal} on the single (per-{@code DirectDraw}-context) encode path.
 * {@code toMessageInternal} resets the overflow counters and builds one proto by sequential
 * {@code addToCache} calls, so serialized encoding is already a correctness requirement independent
 * of this class. Do not assume any method here is safe to call concurrently.
 */
public class LRUDrawConstantPoolCache {

  private final HashMap<DrawConstant<?>, DoubleLinkedListNode> map =
      new HashMap<DrawConstant<?>, DoubleLinkedListNode>();
  private DoubleLinkedListNode head;
  private DoubleLinkedListNode end;
  private int capacity;
  private final int idOffset;
  private final int maxSize;
  // reserve zero id for null constants
  private int nextId = 1;
  /**
   * Current frame stamp. Every entry records the frame in which it was last used. An entry used in
   * the current frame must never be evicted: its id would be recycled within the frame, and the
   * client - which applies a frame's outer constants up front but nested-image constants lazily,
   * i.e. in a different order than they were allocated here - would resolve that id to the wrong
   * constant (the DirectDraw corruption seen with slow clients / large accumulated frames).
   */
  private int frame;

  public LRUDrawConstantPoolCache(int capacity, int idOffset, int maxSize) {
    this.capacity = capacity;
    this.idOffset = idOffset;
    this.maxSize = maxSize;
  }

  public int getCapacity() {
    return capacity;
  }

  /** Starts a new top-level frame (call once per frame, never for nested images). */
  public void startFrame() {
    frame++;
  }

  public void increaseCapacity() {
    capacity *= 2;
    if (capacity > maxSize) {
      throw new RuntimeException("Directdraw: can not process more then " + maxSize
          + " distinct constants in single frame (raise -Dwebswing.ddMaxConstCacheSize)."
          + " Check rendering of swing application.");
    }
  }

  public boolean contains(DrawConstant<?> constant) {
    return map.containsKey(constant);
  }

  public DrawConstant<?> getOrAdd(DrawConstant<?> constant) {
    if (map.containsKey(constant)) {
      DoubleLinkedListNode oldNode = map.get(constant);
      oldNode.remove();
      oldNode.makeHead();
      oldNode.frame = frame;
      return oldNode.getVal();
    } else {
      if (nextId >= capacity && end != null && end.frame == frame) {
        // the LRU victim was used in THIS frame, hence so was every other entry: the frame holds
        // more distinct constants than the cache - grow instead of recycling an in-frame id
        increaseCapacity();
      }
      DoubleLinkedListNode newNode = new DoubleLinkedListNode(constant);
      newNode.frame = frame;
      newNode.makeHead();
      if (nextId >= capacity) {
        // remove oldest node
        int evictedId = map.remove(end.getVal()).getVal().getId();
        end.remove();
        newNode.setId(evictedId);
      } else {
        newNode.setId((nextId++) + idOffset);
      }
      map.put(constant, newNode);
      return constant;
    }
  }

  private class DoubleLinkedListNode {

    private DrawConstant<?> val;
    private int frame;
    private DoubleLinkedListNode pre;
    private DoubleLinkedListNode next;

    public DoubleLinkedListNode(DrawConstant<?> value) {
      val = value;
    }

    public void setId(int id) {
      val.setId(id);
    }

    public DrawConstant<?> getVal() {
      return val;
    }

    public void remove() {
      if (pre != null) {
        pre.next = next;
      } else {
        head = next;
      }
      if (next != null) {
        next.pre = pre;
      } else {
        end = pre;
      }
    }

    public void makeHead() {
      next = head;
      pre = null;
      if (head != null) {
        head.pre = this;
      }
      head = this;
      if (end == null) {
        end = this;
      }
    }
  }
}
