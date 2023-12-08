package com.sopra.compteRendu;

import java.io.IOException;
import java.io.File;
import java.io.FileWriter;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

public class GestonnaireBanque {

	private static final String FILE_NAME = "comptesBancaires.xml";

	public static void main(String[] args) {
		Document doc = getDocument();
//	        initialiserBanque(doc);
//	        lireCompte(doc);

//		CompteBancaire compteExistant = new CompteBancaire(12345, "Rapha", 1500.0, LocalDateTime.now(), "courant");
//		ajouterCompte(compteExistant, doc);
//		ajouterAttributComptes("banque","Credit Agricole",doc);
//		lireCompte(doc);
		int numeroCompte = 1;
		supprimerCompte(numeroCompte, doc);
		lireCompte(doc);
	}

	private static Document getDocument() {
		SAXBuilder builder = new SAXBuilder();
		File xmlFile = new File(FILE_NAME);
		if (xmlFile.exists()) {
			Document jdomDoc;
			try {
				jdomDoc = builder.build(xmlFile);
				return jdomDoc;
			} catch (JDOMException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		Document doc = new Document();
		return doc;
	}

	private static void initialiserBanque(Document doc) {
		try (Scanner scanner = new Scanner(System.in)) {
			doc.setRootElement(new Element("ComptesBancaires"));

			System.out.println("Veuillez entrer le nombre de compte que vous voulez saisir :");
			int nombreCompte = scanner.nextInt();

			for (int i = 0; i < nombreCompte; i++) {
				System.out.println("======================================");
				System.out.println("Compte saisie n°" + i + "\n");

				int numCompte = lireEntier("Veuillez entrer le numéro de compte :", scanner);
				String nomProprietaire = lireChaine("Veuillez entrer le nom du propriétaire :", scanner);
				double solde = lireDouble("Veuillez entrer le solde :", scanner);

				LocalDateTime dateCreation = LocalDateTime.now();

				String typeCompte;
				do {
					typeCompte = lireChaine("Veuillez entrer le type de compte (epargne ou courant) :", scanner)
							.toLowerCase();
				} while (!typeCompte.equals("epargne") && !typeCompte.equals("courant"));

				CompteBancaire nouveauCompte = new CompteBancaire(numCompte, nomProprietaire, solde, dateCreation,
						typeCompte);
				ajouterCompte(nouveauCompte, doc);
				System.out.println("Compte ajouté avec succès !");
			}
		} catch (Exception io) {
			System.out.println(io.getMessage());
		}
	}

	private static int lireEntier(String message, Scanner scanner) {
		System.out.println(message);
		return scanner.nextInt();
	}

	private static String lireChaine(String message, Scanner scanner) {
		System.out.println(message);
		scanner.nextLine();
		return scanner.nextLine();
	}

	private static double lireDouble(String message, Scanner scanner) {
		System.out.println(message);
		return scanner.nextDouble();
	}

	private static void sauvegarderFichierXml(Document doc) throws IOException {
		XMLOutputter xmlOutput = new XMLOutputter();
		xmlOutput.setFormat(Format.getPrettyFormat());
		xmlOutput.output(doc, new FileWriter(FILE_NAME));
		System.out.println("Fichier sauvegardé !");
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

	private static void ajouterCompte(CompteBancaire compteBancaire, Document doc) {
		doc.getRootElement().addContent(createCompteBancaireXMLElement(compteBancaire));

		try {
			sauvegarderFichierXml(doc);
		} catch (IOException io) {
			System.out.println(io.getMessage());
		}
	}

	private static void ajouterAttributComptes(String attributName, String attributValue, Document jdomDoc) {
		try {
			Element root = jdomDoc.getRootElement();
			List<Element> listOfComptes = root.getChildren("CompteBancaire");

			for (Element element : listOfComptes) {
				element.setAttribute(attributName, attributValue);
			}
			sauvegarderFichierXml(jdomDoc);
			System.out.println("Attribut ajouté à tous les comptes avec succès !");
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	private static void lireCompte(Document doc) {
		try (Scanner scanner = new Scanner(System.in)) {
			int choix;
			do {
				System.out.println("Veuillez entrer votre choix :");
				System.out.println("1 - Lister tous les comptes");
				System.out.println("2 - Lister seulement les comptes courants");
				choix = scanner.nextInt();
			} while (choix != 1 && choix != 2);

			afficherComptes(choix, doc);
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	private static void supprimerCompte(int numeroCompte, Document jdomDoc) {
		try {
			Element root = jdomDoc.getRootElement();
			List<Element> listOfComptes = root.getChildren("CompteBancaire");

			Element compteASupprimer = null;
			for (Element element : listOfComptes) {
				if (Integer.parseInt(element.getChildText("numCompte")) == numeroCompte) {
					compteASupprimer = element;
					break;
				}
			}

			if (compteASupprimer != null) {
				listOfComptes.remove(compteASupprimer);
				sauvegarderFichierXml(jdomDoc);
				System.out.println("Compte supprimé avec succès !");
			} else {
				System.out.println("Aucun compte trouvé avec le numéro de compte spécifié.");
			}
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	private static void afficherComptes(int choix, Document jdomDoc) {
		try {
			Element root = jdomDoc.getRootElement();
			List<Element> listOfComptes = root.getChildren("CompteBancaire");

			for (Element element : listOfComptes) {
				String typeCompte = element.getChildText("typeCompte");

				if (typeCompte.equals("courant") && choix == 2)
					continue;

				String numCompte = element.getChildText("numCompte");
				String solde = element.getChildText("solde");
				String dateCreation = element.getChildText("dateCreation");
				String nomProprietaire = element.getChildText("nomProprietaire");

				System.out.println("Numero Compte : " + numCompte + " --- Nom du propriétaire :  " + nomProprietaire
						+ " --- Solde : " + solde + " --- dateCreation : " + dateCreation + " --- Type de compte : "
						+ typeCompte);
			}
		} catch (Exception e) {
			System.out.println(e);
		}
	}
}