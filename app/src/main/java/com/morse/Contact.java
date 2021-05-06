package com.morse;

import com.channels.androidsms.MessageInfo;

import java.util.List;

/**
 *
 */
public interface Contact {

    /**
     * @param from
     * @return
     */
    List<MessageInfo> getMessages(String from);

}