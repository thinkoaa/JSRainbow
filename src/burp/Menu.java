package burp;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JMenuItem;

public class Menu implements IContextMenuFactory{
    private int[] selectRange;

    public List<JMenuItem> createMenuItems(final IContextMenuInvocation invocation) {
        List<JMenuItem> menus = new ArrayList<JMenuItem>();

        selectRange = invocation.getSelectionBounds();
        if(invocation.getInvocationContext() != IContextMenuInvocation.CONTEXT_MESSAGE_EDITOR_REQUEST || selectRange[0] == selectRange[1]){
            return menus;
        }

        JMenuItem excuteMenu = new JMenuItem(BurpExtender.extensionName);

        excuteMenu.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                IHttpRequestResponse reqRsp = invocation.getSelectedMessages()[0];
                byte[] byteReq = reqRsp.getRequest();
                try {
                    String strReq = new String(byteReq);
                    String strSelect = new String(byteReq, selectRange[0],(selectRange[1]-selectRange[0]), "UTF-8");
                    String strEncrypt = Utils.sendPayload(strSelect);
                    StringBuffer sbReq = new StringBuffer(strReq);
                    sbReq.replace(selectRange[0],selectRange[1],strEncrypt);
                    byte[] newRequst = BurpExtender.helpers.stringToBytes(sbReq.toString());
                    reqRsp.setRequest(newRequst);
                } catch (UnsupportedEncodingException er) {
                    BurpExtender.stderr.println(er.getMessage());
                }
            }
        });

        menus.add(excuteMenu);
        return menus;
    }
}
