package com.sopra.compteRendu;

import java.io.File;
import java.util.List;
import java.util.Scanner;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;



public class LireCompte {

	public static void main(String[] args) {
		final String fileName = "comptesBancaires.xml";
		try (Scanner scanner = new Scanner (System.in)) {
			int choix;
		    do {
		    	System.out.println("Veuillez entrer votre choix :");
		    	System.out.println("1 - Lister tous les comptes");
		    	System.out.println("2 - Lister seulement les comptes courants");
		    	choix = scanner.nextInt();
		    } while(choix != 1 && choix !=2);
			SAXBuilder builder = new SAXBuilder();
			File xmlFile = new File(fileName);
			Document jdomDoc = (Document) builder.build(xmlFile);
			
			Element root = jdomDoc.getRootElement();
			List <Element> listOfComptes = root.getChildren("CompteBancaire");
			for(Element element : listOfComptes) {
				String typeCompte = element.getChildText("typeCompte");
				if (typeCompte.equals("courant") && choix==2 ) continue ;
				String numCompte = element.getChildText("numCompte");
				String solde = element.getChildText("solde");
				String dateCreation = element.getChildText("dateCreation");
				String nomProprietaire  = element.getChildText("nomProprietaire");
				System.out.println("Numero Compte : "+ numCompte + " --- Nom du propriétaire :  "+ nomProprietaire + " --- Solde : "+ solde +" --- dateCreation : "+dateCreation +" --- Type de compte : "+typeCompte);
			}
		} catch(Exception e) {
			System.out.println(e);
			
		}


	}

}
