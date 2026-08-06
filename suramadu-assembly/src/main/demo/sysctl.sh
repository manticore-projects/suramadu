# Tune the Linux servers for the lossy high-RTT link.
# BBR is the big one on a lossy intercontinental path; fq is its companion qdisc;
# the buffers cover the bandwidth-delay product (size to your real RTT×bandwidth — measure RTT with ping/ss);
# tcp_mtu_probing guards against broken PMTU through the tunnel.


sudo tee /etc/sysctl.d/99-suramadu-wan.conf >/dev/null <<'EOF'
net.core.default_qdisc = fq
net.ipv4.tcp_congestion_control = bbr
net.core.rmem_max = 16777216
net.core.wmem_max = 16777216
net.ipv4.tcp_rmem = 4096 87380 16777216
net.ipv4.tcp_wmem = 4096 65536 16777216
net.ipv4.tcp_mtu_probing = 1
EOF
sudo modprobe tcp_bbr                      # usually auto-loaded when CC is set, but be explicit
sudo sysctl --system                       # applies all drop-ins now; re-applied at boot
sudo tc qdisc replace dev <iface> root fq  # switch the already-up interface without a reboot