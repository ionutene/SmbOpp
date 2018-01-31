package org.study.serverHack;

import java.io.*;
import java.net.MalformedURLException;
import java.net.UnknownHostException;

import jcifs.UniAddress;
import jcifs.smb.*;


public class SMBOpp {

    public static void main(String[] args) {
        String userName1 = "user1";
        String password1 = "pass1";
        String userName2 = "user2";
        String password2 = "pass2";

        NtlmPasswordAuthentication ntlmPasswordAuthentication1 = new NtlmPasswordAuthentication("", userName1, password1);
        NtlmPasswordAuthentication ntlmPasswordAuthentication2 = new NtlmPasswordAuthentication("", userName2, password2);
        String ip1 = "10.XX.XX.XX";
        String ip2 = "192.XX.XX.XX";

        String smbSource1 = "smb://" + ip1 + "//D$/sharedFolder/sharedFile.file";
        String smbSource2 = "smb://" + ip2 + "/sharedFolder/sharedFile.file";
        try {
            System.out.println(checkLogin(ip1, ntlmPasswordAuthentication1));
            System.out.println(checkLogin(ip2, ntlmPasswordAuthentication2));
            checkConnection(ntlmPasswordAuthentication2, smbSource1);
            copyFrom1to2(ntlmPasswordAuthentication1, smbSource1, ntlmPasswordAuthentication2, smbSource2);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }


    public static void copyFrom1to2(NtlmPasswordAuthentication ntlmPasswordAuthentication1, String smbSource1, NtlmPasswordAuthentication ntlmPasswordAuthentication2,
            String smbSource2) throws IOException {
        try {
            SmbFile source = new SmbFile(smbSource1, ntlmPasswordAuthentication1);

            SmbFile destination = new SmbFile(smbSource2, ntlmPasswordAuthentication2);

            //--------- final process to move folder
            source.copyTo(destination);

            //--------- local moving folder
           /* InputStream initialStream = destination.getInputStream();
            byte[] buffer = new byte[initialStream.available()];
            initialStream.read(buffer);

            File targetFile = new File("src/main/resources/test2.txt");
            OutputStream outStream = new FileOutputStream(targetFile);
            outStream.write(buffer);*/
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (SmbException e) {
            e.printStackTrace();
        }

    }

    public static boolean checkLogin(String ip, NtlmPasswordAuthentication mycreds) throws UnknownHostException {
        UniAddress mydomaincontroller = UniAddress.getByName(ip);
        try {
            SmbSession.logon(mydomaincontroller, mycreds);
            // SUCCESS
            return true;
        } catch (SmbAuthException sae) {
            // AUTHENTICATION FAILURE
            return false;
        } catch (SmbException se) {
            // NETWORK PROBLEMS?
            se.printStackTrace();
        }
        return false;
    }


    public static void checkConnection(NtlmPasswordAuthentication auth, String path) throws IOException {


        SmbFile smbFile = new SmbFile(path, auth);
        SmbFileOutputStream smbfos = new SmbFileOutputStream(smbFile);
        smbfos.write("testing....and writing to a file".getBytes());
        System.out.println("completed ...nice !");

    }
}
