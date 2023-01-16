package org.example.Network;

import java.net.Socket;

public class Communication {

    private ListenMessageTCPThread listenMessageTCPThread;
    private SendMessageTCPThread sendMessageTCPThread;

    public ListenMessageTCPThread getListenMessageTCPThread() {
        return listenMessageTCPThread;
    }

    public SendMessageTCPThread getSendMessageTCPThread() {
        return sendMessageTCPThread;
    }

    public Communication(ListenMessageTCPThread listenMessageTCPThread, SendMessageTCPThread sendMessageTCPThread) {
        this.listenMessageTCPThread = listenMessageTCPThread;
        this.sendMessageTCPThread = sendMessageTCPThread;

    }
}
