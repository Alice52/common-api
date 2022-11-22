package common.uid.utils;

import common.core.constant.enums.CommonResponseEnum;
import common.core.exception.BaseException;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

/**
 * @author zack <br>
 * @create 2021-06-23<br>
 * @project project-custom <br>
 */
public abstract class NetUtils {

    /** Pre-loaded local address */
    public static InetAddress localAddress;

    /**
     * Retrieve the first validated local ip address(the Public and LAN ip addresses are validated).
     *
     * @return the local address
     * @throws SocketException the socket exception
     */
    public static InetAddress getLocalInetAddress() throws SocketException {
        // enumerates all network interfaces
        Enumeration<NetworkInterface> enu = NetworkInterface.getNetworkInterfaces();

        while (enu.hasMoreElements()) {
            NetworkInterface ni = enu.nextElement();
            if (ni.isLoopback()) {
                continue;
            }

            Enumeration<InetAddress> addressEnumeration = ni.getInetAddresses();
            while (addressEnumeration.hasMoreElements()) {
                InetAddress address = addressEnumeration.nextElement();
                // ignores all invalidated addresses
                if (address.isLinkLocalAddress()
                        || address.isLoopbackAddress()
                        || address.isAnyLocalAddress()) {
                    continue;
                }

                return address;
            }
        }

        throw new BaseException(
                CommonResponseEnum.UID_GENERATE_ERROR, "No validated local address!");
    }

    /**
     * Retrieve local address
     *
     * @return the string local address
     */
    public static String getLocalAddress() {
        return localAddress.getHostAddress();
    }

    static {
        try {
            localAddress = getLocalInetAddress();
        } catch (SocketException e) {
            throw new RuntimeException("fail to get local ip.");
        }
    }
}
