#!/usr/bin/env python3
"""Summarize a BenchmarkStats CSV per scene.

Usage: analyze_benchmark.py /path/to/benchmark.csv

Joins FRAME rows (server build) with ACK rows (client render + RTT) on the frame's
sendTimestamp, segments by SCENE markers, and prints p50/p95/p99 per scene for:
  extract_ms   time under AWT treeLock + webPaintLock (EDT-contention cost)
  encode_ms    ZPNG + protobuf encoding (off-lock, sender thread)
  render_ms    client-measured decode + canvas replay (client clock, duration only)
  rtt_ms       sendTimestamp -> timestamp-echo arrival (server clock; includes network
               both ways + client processing; rtt - render ~= network + queueing)
  kb/frame     DirectDraw payload bytes
plus achieved frames/second within the scene window.
"""
import csv
import sys
from bisect import bisect_right


def pct(sorted_vals, p):
    if not sorted_vals:
        return float("nan")
    k = max(0, min(len(sorted_vals) - 1, round(p / 100 * (len(sorted_vals) - 1))))
    return sorted_vals[k]


def main(path):
    scenes = []  # (ts, name)
    frames = {}  # sendTs -> (extract_ns, encode_ns, dd_bytes)
    acks = {}    # sendTs -> (recv_ts, render_ms, ping, rtt_ms)
    with open(path, newline="") as f:
        for row in csv.reader(f):
            if not row or row[0] == "type":
                continue
            t = row[0]
            if t == "SCENE":
                scenes.append((int(row[1]), row[2]))
            elif t == "FRAME":
                try:
                    frames[int(row[1])] = (int(row[2]), int(row[3]), int(row[4]))
                except ValueError:
                    pass
            elif t == "ACK":
                try:
                    send = int(row[2])
                    render = float(row[4]) if row[4] not in ("", "null", "None") else float("nan")
                    rtt = int(row[6])
                    acks[send] = (int(row[1]), render, rtt)
                except ValueError:
                    pass

    if not scenes:
        scenes = [(min(frames, default=0), "ALL")]
    bounds = [ts for ts, _ in scenes]

    per_scene = {name: {"extract": [], "encode": [], "render": [], "rtt": [], "bytes": [], "n": 0,
                        "t0": ts, "t1": None} for ts, name in scenes}
    order = [name for _, name in scenes]
    for i, (ts, name) in enumerate(scenes):
        per_scene[name]["t1"] = scenes[i + 1][0] if i + 1 < len(scenes) else None

    for send, (ext, enc, bts) in sorted(frames.items()):
        i = bisect_right(bounds, send) - 1
        if i < 0:
            continue
        s = per_scene[order[i]]
        s["n"] += 1
        s["last"] = send
        s["extract"].append(ext / 1e6)
        s["encode"].append(enc / 1e6)
        s["bytes"].append(bts)
        if send in acks:
            _, render, rtt = acks[send]
            if render == render:  # not NaN
                s["render"].append(render)
            if rtt >= 0:
                s["rtt"].append(rtt)

    hdr = f"{'scene':<12}{'frames':>7}{'fps':>7} | {'metric':<10}{'p50':>8}{'p95':>8}{'p99':>8}{'max':>9}"
    print(hdr)
    print("-" * len(hdr))
    for name in order:
        s = per_scene[name]
        # scene without an end marker (the last one): use its last frame as the end
        end = s["t1"] if s["t1"] is not None else s.get("last", s["t0"])
        dur = (end - s["t0"]) / 1000.0
        fps = s["n"] / dur if dur > 0 else float("nan")
        first = True
        for metric in ("extract", "encode", "render", "rtt"):
            vals = sorted(s[metric])
            label = f"{name:<12}{s['n']:>7}{fps:>7.1f}"[:26].ljust(26) if first else " " * 26
            first = False
            print(f"{label} | {metric + '_ms':<10}{pct(vals, 50):>8.2f}{pct(vals, 95):>8.2f}"
                  f"{pct(vals, 99):>8.2f}{(vals[-1] if vals else float('nan')):>9.2f}")
        kb = sorted(v / 1024.0 for v in s["bytes"])
        print(f"{' ' * 26} | {'kb/frame':<10}{pct(kb, 50):>8.1f}{pct(kb, 95):>8.1f}"
              f"{pct(kb, 99):>8.1f}{(kb[-1] if kb else float('nan')):>9.1f}")
        print()


if __name__ == "__main__":
    if len(sys.argv) != 2:
        print(__doc__)
        sys.exit(2)
    main(sys.argv[1])