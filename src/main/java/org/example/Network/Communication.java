package org.example.Network;


public class Communication {

    private final ListenMessageTCPThread listenMessageTCPThread;
    private final SendMessageTCPThread sendMessageTCPThread;

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
