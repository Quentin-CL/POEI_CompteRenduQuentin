package com.sopra.compteRendu;


import java.io.IOException;
import java.io.FileWriter;
import java.time.LocalDate;
import java.util.Scanner;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

public class SaisieCompte {

	public static void main(String[] args) {
		try (Scanner scanner = new Scanner(System.in)){
        Document doc = new Document();
        doc.setRootElement(new Element("ComptesBancaires"));	
		
        System.out.println("Veuillez entrer le nombre de compte que vous voulez saisir :");
        int nombreCompte = scanner.nextInt();
        for(int i=0; i < nombreCompte; i++) {
        	System.out.println("======================================");
        	System.out.println("Compte saisie n°"+i+"\n");
		    // Lire les attributs du compte bancaire depuis le clavier
		    System.out.println("Veuillez entrer le numéro de compte :");
		    int numCompte = scanner.nextInt();
		    scanner.nextLine();
		    System.out.println("Veuillez entrer le nom du propriétaire :");
		    String nomProprietaire = scanner.nextLine();
		    System.out.println("Veuillez entrer le solde :");
		    double solde = scanner.nextDouble();
		    scanner.nextLine(); 
		    System.out.println("Veuillez entrer la date de création (AAAA-MM-JJ) :");
		    LocalDate dateCreation = LocalDate.parse(scanner.nextLine());
		    String typeCompte;
		    do {
		    	System.out.println("Veuillez entrer le type de compte (epargne ou courant) :");
		    	typeCompte = scanner.nextLine();
		    	typeCompte.toLowerCase();
		    } while(!typeCompte.equals("epargne") && !typeCompte.equals("courant"));
		
		    CompteBancaire compteBancaire = new CompteBancaire(numCompte, nomProprietaire, solde, dateCreation, typeCompte);
		    			doc.getRootElement().addContent(createCompteBancaireXMLElement(compteBancaire));
		}
        
        XMLOutputter xmlOutput = new XMLOutputter();
        xmlOutput.setFormat(Format.getPrettyFormat());
        xmlOutput.output(doc, new FileWriter("comptesBancaires.xml"));
        System.out.println("Fichier sauvegardé !");
		} catch (IOException io) {
			System.out.println(io.getMessage());
		}
	}
	 private static Element createCompteBancaireXMLElement(CompteBancaire compteBancaire) {
	        Element compteBancaireElem = new Element("CompteBancaire");
	        compteBancaireElem.addContent(new Element("numCompte").setText(String.valueOf(compteBancaire.getNumCompte())));
	        compteBancaireElem.addContent(new Element("nomProprietaire").setText(compteBancaire.getNomProprietaire()));
	        compteBancaireElem.addContent(new Element("solde").setText(String.valueOf(compteBancaire.getSolde())));
	        compteBancaireElem.addContent(new Element("dateCreation").setText(compteBancaire.getDateCreation().toString()));
	        compteBancaireElem.addContent(new Element("typeCompte").setText(compteBancaire.getTypeCompte()));
	        return compteBancaireElem;
	    }

}