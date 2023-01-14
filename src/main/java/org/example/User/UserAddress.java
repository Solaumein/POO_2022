package org.example.User;

import java.net.InetAddress;

public class UserAddress {
    private InetAddress address;
    private int port;
    public UserAddress(InetAddress address,int port){
        this.address=address;
        this.port=port;
    }
    public InetAddress getAddress() {
        return address;
    }
    public int getPort() {
        return port;
    }

    @Override
    public String toString() {
        return "UserAddress{" +
                "address=" + address +
                ", port=" + port +
                '}';
    }
}
