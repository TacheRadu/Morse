package com.morse;

import java.util.List;
import com.channels.androidsms.MessageInfo;


/**
 * Interface that provides methods to be implemented by contact classes of the channels added to
 * Morse.
 *
 * @version 0.1.1
 */
public interface Contact {
    List<MessageInfo> getMessages(String from);
}