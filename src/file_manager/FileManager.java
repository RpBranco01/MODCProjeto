package file_manager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.AlgorithmParameters;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class FileManager {

	private static final String PBE = "PBEWithHmacSHA256AndAES_128";
	private File file;
	private List<String> listaContas;
	private Cipher cipher;
	private SecretKey key;
	private byte[] params;
	
	public FileManager(String filePath, String serverPassword) {
		this.file = new File(filePath);
		this.listaContas = new ArrayList<>();
		byte[] salt = { (byte) 0xc9, (byte) 0x36, (byte) 0x78, (byte) 0x99, (byte) 0x52, (byte) 0x3e, (byte) 0xea, (byte) 0xf2 };
	    PBEKeySpec keySpec = new PBEKeySpec(serverPassword.toCharArray(), salt, 20); // pass, salt, iterations
	    try{
	        SecretKeyFactory kf = SecretKeyFactory.getInstance(PBE);
	        this.key = kf.generateSecret(keySpec);
	    } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
	        e.printStackTrace();
	    }
	}
	
	public void createFile(){
		try {
			if (file.createNewFile()) {
			  System.out.println("File created: " + file.getName());
			} else {
			  System.out.println("File already exists.");
			}
		  } catch (IOException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		  }
	}
	
	public void appendCount(String result) {
		listaContas.add(result);
	}
	
	public synchronized void cipherCounts() {
	    /*Cifrar o users.txt*/
	    try{
	        this.cipher = Cipher.getInstance(PBE);
	        this.cipher.init(Cipher.ENCRYPT_MODE, this.key);
	        this.params = cipher.getParameters().getEncoded();
	        writeParamsCountsToFile();
	
	
	        FileOutputStream fos = new FileOutputStream("counts.cif");
	        CipherOutputStream cos = new CipherOutputStream(fos, this.cipher);
	        ObjectOutputStream oos = new ObjectOutputStream(cos);
	
	        oos.writeObject(listaContas);
	
	        oos.close();
	        cos.close();
	        fos.close();
	
	    }catch(Exception e){
	        e.printStackTrace();
	    }
	}

	public void deCipherCounts() {
	    /*Decifrar users.cif*/
	    try{
	        if(Files.exists(Paths.get("params.count"))) {
	            getParamsCountsFromFile();
	        }
	
	        AlgorithmParameters p = AlgorithmParameters.getInstance(PBE);
	        p.init(this.params);
	        Cipher d = Cipher.getInstance(PBE);
	        d.init(Cipher.DECRYPT_MODE, this.key, p);
	
	        FileInputStream fic = new FileInputStream("counts.cif");
	        CipherInputStream cis = new CipherInputStream(fic, d);
	        this.listaContas = (List<String>) new ObjectInputStream(cis).readObject();
	    }catch (Exception e){
	        e.printStackTrace();
	    }
	}

	private void writeParamsCountsToFile(){
        try(FileOutputStream fileOut = new FileOutputStream("params.count")) {
            ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
            objectOut.writeObject(this.params);
            objectOut.flush();
            objectOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void getParamsCountsFromFile() {
        try (FileInputStream fileIn = new FileInputStream("params.count")) {
            ObjectInputStream objectIn = new ObjectInputStream(fileIn);

            this.params = (byte[]) objectIn.readObject();
            objectIn.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

	public void getListCounts() {
		for (String string : listaContas) {
			System.out.println(string);
		}
	}
}
