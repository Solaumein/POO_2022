package org.example;

import java.net.InetAddress;

public class UserAddress {
    private InetAddress address;
    private int port;
    UserAddress(InetAddress address,int port){
        this.address=address;
        this.port=port;
    }
    public InetAddress getAddress() {
        return address;
    }
    public int getPort() {
        return port;
    }
}
