package it.swb.bean;

import it.swb.business.McdBusiness;
import it.swb.database.Articolo_DAO;
import it.swb.database.Mcd_DAO;
import it.swb.java.EditorModelliAmazon;
import it.swb.log.Log;
import it.swb.model.Articolo;
import it.swb.utility.Methods;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.List;
import java.util.Properties;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

@ManagedBean(name = "mcdBean")
@ViewScoped
public class McdBean  implements Serializable  {

	private static final long serialVersionUID = 1L;
	
	private int articoliInAttesaAmazon = 0;
	private int articoliInAttesaYatego = 0;
	private StreamedContent fileAmazon;
	private StreamedContent fileYatego; 
	private String nomeFile;
	private String percorsoFile;
	
	public void creaModelloAmazon(){
		Properties config = new Properties();	   
		List<String> codici_articoli = McdBusiness.getMcd("amazon");
		
		try {
			config.load(Log.class.getResourceAsStream("/zeus.properties"));
			
			percorsoFile = config.getProperty("percorso_mcd");	
			nomeFile = config.getProperty("nome_mcd_amazon");
			
			String data = Methods.getDataCompletaPerNomeFileTesto();
			
			nomeFile = nomeFile.replace("DATA", data);
			
			List<Articolo> articoli = Articolo_DAO.getArticoliByCodice(codici_articoli);
			
			for (Articolo a : articoli){
				EditorModelliAmazon.aggiungiAModelloAmazon(a,percorsoFile+nomeFile);
			}
			
			Mcd_DAO.segnaComeElaborati("amazon");
			
		} catch (IOException e) {
			e.printStackTrace();
			Log.error(e.getMessage());
		}		
		
	}
	
	public void creaModelloYatego(){
		
		//List<String> articoli = McdBusiness.getMcd("yatego");
	}
	
	public void segnaComeElaboratiAmazon(){
		
		
	}
	
	public void segnaComeElaboratiYatego(){
		
		
	}
  
    public StreamedContent getFileAmazon() throws FileNotFoundException {  
    	creaModelloAmazon();
    	
    	Log.info("Download file: "+getPercorsoFile()+getNomeFile());
    	
    	if (percorsoFile!=null && !percorsoFile.isEmpty() && nomeFile!=null && !nomeFile.isEmpty()){
    		
    		File f = new File(percorsoFile+nomeFile);
            
            if (f.exists()){
            	InputStream stream = new FileInputStream(f);
            	
            	if (nomeFile.contains(".csv"))
            		fileAmazon = new DefaultStreamedContent(stream, "text/csv", nomeFile);  
                else if (nomeFile.contains(".txt"))
                	fileAmazon = new DefaultStreamedContent(stream, "text/txt", nomeFile); 
            }
    	}
    	
        return fileAmazon;  
    }  
    
    public StreamedContent getMcdYatego() throws FileNotFoundException {  
    	creaModelloAmazon();
    	
    	Log.info("Download file: "+getPercorsoFile()+getNomeFile());
    	
    	if (percorsoFile!=null && !percorsoFile.isEmpty() && nomeFile!=null && !nomeFile.isEmpty()){
    		
    		File f = new File(percorsoFile+nomeFile);
            
            if (f.exists()){
            	InputStream stream = new FileInputStream(f);
            	
            	if (nomeFile.contains(".csv"))
            		fileYatego = new DefaultStreamedContent(stream, "text/csv", nomeFile);  
                else if (nomeFile.contains(".txt"))
                	fileYatego = new DefaultStreamedContent(stream, "text/txt", nomeFile); 
            }
    	}
    	
        return fileYatego;  
    } 
	
	
	public int getArticoliInAttesaAmazon() {
		articoliInAttesaAmazon = McdBusiness.getNumeroArticoliInAttesa("amazon");
		return articoliInAttesaAmazon;
	}
	public void setArticoliInAttesaAmazon(int articoliInAttesaAmazon) {
		this.articoliInAttesaAmazon = articoliInAttesaAmazon;
	}
	public int getArticoliInAttesaYatego() {
		articoliInAttesaYatego = McdBusiness.getNumeroArticoliInAttesa("yatego");
		return articoliInAttesaYatego;
	}
	public void setArticoliInAttesaYatego(int articoliInAttesaYatego) {
		this.articoliInAttesaYatego = articoliInAttesaYatego;
	}

	public void setFileAmazon(StreamedContent fileAmazon) {
		this.fileAmazon = fileAmazon;
	}
	
	public void setFileYatego(StreamedContent fileYatego) {
		this.fileYatego = fileYatego;
	}

	public String getNomeFile() {
		return nomeFile;
	}

	public void setNomeFile(String nomeFile) {
		this.nomeFile = nomeFile;
	}

	public String getPercorsoFile() {
		Properties config = new Properties();	   
		try {
			config.load(Log.class.getResourceAsStream("/zeus.properties"));
			
			percorsoFile = config.getProperty("percorso_mcd");		
		} catch (IOException e) {
			e.printStackTrace();
			Log.error(e.getMessage());
		}		
		return percorsoFile;
	}

	public void setPercorsoFile(String percorsoFile) {
		this.percorsoFile = percorsoFile;
	}
	
	
	

}
