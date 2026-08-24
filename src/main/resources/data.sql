-- =========================================================
-- DeclarPro — Données d'exemple pour les tables de référence
-- À placer dans : src/main/resources/data.sql
-- =========================================================

-- ---------------------------------------------------------
-- PORT_ENTREE
-- ---------------------------------------------------------
INSERT INTO port_entree (id, code, nom, ville, active) VALUES
                                                           (1, 'TANGER-MED', 'Tanger Med', 'Tanger', true),
                                                           (2, 'TANGER-VILLE', 'Port de Tanger', 'Tanger', true),
                                                           (3, 'NADOR', 'Port de Nador', 'Nador', true),
                                                           (4, 'AL-HOCEIMA', 'Port d''Al Hoceïma', 'Al Hoceïma', true),
                                                           (5, 'CASABLANCA', 'Port de Casablanca', 'Casablanca', true),
                                                           (6, 'MOHAMMEDIA', 'Port de Mohammedia', 'Mohammedia', true),
                                                           (7, 'JORF-LASFAR', 'Port de Jorf Lasfar', 'El Jadida', true),
                                                           (8, 'SAFI', 'Port de Safi', 'Safi', true),
                                                           (9, 'AGADIR', 'Port d''Agadir', 'Agadir', true),
                                                           (10, 'KENITRA', 'Port de Kénitra', 'Kénitra', true),
                                                           (11, 'TAN-TAN', 'Port de Tan-Tan', 'Tan-Tan', true),
                                                           (12, 'LAAYOUNE', 'Port de Laâyoune', 'Laâyoune', true),
                                                           (13, 'DAKHLA', 'Port de Dakhla', 'Dakhla', true)
ON CONFLICT (id) DO NOTHING;

-- ---------------------------------------------------------
-- ORGANISME
-- ---------------------------------------------------------
INSERT INTO organisme (id, code, nom, description, active) VALUES
                                                               (1, 'ADII', 'Administration des Douanes et Impôts Indirects', 'Autorité douanière marocaine', true),
                                                               (2, 'ONSSA', 'Office National de Sécurité Sanitaire des Aliments', 'Contrôle sanitaire des produits alimentaires', true),
                                                               (3, 'OC', 'Office des Changes', 'Contrôle des opérations de change', true),
                                                               (4, 'MI', 'Ministère de l''Industrie', 'Autorisations industrielles', true),
                                                               (5, 'ANRT', 'Agence Nationale de Réglementation des Télécommunications', 'Homologation des équipements émettant ou recevant des ondes (drones, WiFi, objets connectés) avant importation', true)
ON CONFLICT (id) DO NOTHING;

-- ---------------------------------------------------------
-- REFERENCE_JURIDIQUE
-- ---------------------------------------------------------
INSERT INTO reference_juridique (id, code, type_texte, numero, titre, date_publication, date_entree_vigueur, date_fin_validite, source, description, active) VALUES
                                                                                                                                                                 (1, 'CDI-2020', 'Code', '01-77', 'Code des Douanes et Impôts Indirects', '1977-10-09', '1977-10-09', NULL, 'Bulletin Officiel', 'Texte de référence des douanes marocaines', true),
                                                                                                                                                                 (2, 'CIRC-4560', 'Circulaire', '4560/222', 'Circulaire relative aux régimes économiques en douane', '2015-03-12', '2015-03-12', NULL, 'ADII', 'Précise les modalités des régimes suspensifs', true),
                                                                                                                                                                 (3, 'LOI-CHANGE', 'Loi', '30-15', 'Loi relative à l''Office des Changes', '2018-06-01', '2018-06-01', NULL, 'Bulletin Officiel', 'Encadrement des opérations de change', true),
                                                                                                                                                                 (4, 'LOI-13-89', 'Loi', '13-89', 'Loi relative au commerce extérieur', '1992-11-09', '1992-11-09', NULL, 'Dahir n° 1-91-261 du 13 joumada I 1413', 'Loi fondamentale regissant les engagements, licences d''importation/exportation et franchises douanieres', true),
                                                                                                                                                                 (5, 'DEC-93-415', 'Decret', '2-93-415', 'Decret pris pour l''application de la loi 13-89 relative au commerce exterieur', '1993-07-02', '1993-07-02', NULL, 'Bulletin Officiel N4212 du 21.07.1993, modifie BO N4796 du 18.05.2000', 'Fixe les modalites d''importation/exportation, licences, franchises douanieres, mesures de sauvegarde (anti-dumping)', true),
                                                                                                                                                                 (6, 'ARR-1675-15', 'Arrete', '1675-15', 'Arrete fixant les modalites de souscription des titres d''importation et d''exportation', '2015-05-19', '2015-05-19', NULL, 'Bulletin Officiel N6366 du 4-6-2015', 'Fixe la souscription electronique via Portnet des engagements/licences d''importation, duree de validite 6 mois', true)
ON CONFLICT (id) DO NOTHING;

-- ---------------------------------------------------------
-- DOCUMENT_REFERENTIEL
-- ---------------------------------------------------------
INSERT INTO document_referentiel (id, organisme_id, code, libelle, type_portnet, delai_jours, critique, description, active) VALUES
                                                                                                                                 (1, 1, 'DCE', 'Déclaration Complémentaire à l''Engagement', 'DCE', 5, true, 'Document obligatoire pour la domiciliation', true),
                                                                                                                                 (2, 1, 'FACT-COM', 'Facture Commerciale', 'FACTURE', 3, true, 'Facture fournisseur originale', true),
                                                                                                                                 (3, 2, 'CERT-SANIT', 'Certificat Sanitaire', 'CERTIFICAT', 10, true, 'Requis pour les produits alimentaires', true),
                                                                                                                                 (4, 3, 'ATTEST-CHANGE', 'Attestation de l''Office des Changes', 'ATTESTATION', 15, false, 'Requise pour certains transferts de devises', true),
                                                                                                                                 (5, 1, 'CERT-ORIGINE', 'Certificat d''Origine', 'CERTIFICAT', 7, false, 'Requis pour bénéficier de préférences tarifaires', true),
                                                                                                                                 (6, 1, 'PACK-LIST', 'Liste de Colisage', 'PACKING_LIST', 3, true, 'Detaille le contenu, poids et dimensions des colis expedies ; document commercial standard toujours requis', true),
                                                                                                                                 (7, 1, 'CONN-BL', 'Connaissement (Bill of Lading)', 'TRANSPORT', 3, true, 'Titre de transport maritime prouvant la prise en charge de la marchandise par le transporteur', true),
                                                                                                                                 (8, 4, 'ENG-IMPORT', 'Engagement d''Importation', 'ENGAGEMENT', 5, true, 'Declaration domiciliee aupres d''une banque, valable 6 mois (Arrete 1675-15 Art.5), couvre la majorite des marchandises libres a l''importation', true),
                                                                                                                                 (9, 4, 'LIC-IMPORT', 'Licence d''Importation', 'LICENCE', 30, false, 'Requise pour les marchandises soumises a restriction ; decision notifiee sous 30 jours (Decret 2-93-415 Art.4)', true),
                                                                                                                                 (10, 4, 'DPI', 'Declaration Prealable d''Importation', 'DECLARATION_PREALABLE', 20, false, 'Requise pour certaines marchandises specifiques ; instruite en 10 a 20 jours (Decret 2-93-415 Art.27-28)', true),
                                                                                                                                 (11, 4, 'FRANCHISE-DOUANE', 'Demande de Franchise Douaniere', 'FRANCHISE', 30, false, 'A soumettre pour beneficier d''une exoneration des droits de douane sur certaines marchandises', true),
                                                                                                                                 (12, 4, 'CUF', 'Certificat d''Utilisation Finale', 'CERTIFICAT', 15, false, 'Certifie que les biens importes sont destines au marche marocain et ne seront pas reexportes vers un pays tiers (biens sensibles/double usage)', true)
ON CONFLICT (id) DO NOTHING;

-- ---------------------------------------------------------
-- REGLE_DOCUMENTAIRE
-- ---------------------------------------------------------
INSERT INTO regle_documentaire (id, document_id, reference_juridique_id, dimension, value, obligatoire, active, description) VALUES
                                                                                                                                 (1, 1, 2, 'delai', '5', true, true, 'Le DCE doit être soumis dans les 5 jours ouvrables'),
                                                                                                                                 (2, 2, 1, 'montant', '>0', true, true, 'La facture doit avoir un montant positif'),
                                                                                                                                 (3, 3, 1, 'categorie', 'ALIMENTAIRE', true, true, 'Certificat sanitaire obligatoire pour les produits alimentaires'),
                                                                                                                                 (4, 4, 3, 'montant', '>100000', false, true, 'Attestation de change requise au-delà de 100 000 MAD'),
                                                                                                                                 (5, 6, 1, 'global', 'true', true, true, 'La liste de colisage est obligatoire pour toute importation'),
                                                                                                                                 (6, 7, 1, 'global', 'true', true, true, 'Le connaissement (B/L) est obligatoire pour toute importation par voie maritime'),
                                                                                                                                 (7, 8, 6, 'global', 'true', true, true, 'L''engagement d''importation est obligatoire sauf marchandises dispensees'),
                                                                                                                                 (8, 9, 5, 'produit_soumis_licence', 'true', false, true, 'Requise uniquement si le produit figure sur la liste des marchandises soumises a licence'),
                                                                                                                                 (9, 10, 5, 'produit_soumis_declaration_prealable', 'true', false, true, 'Requise uniquement pour les marchandises soumises a declaration prealable'),
                                                                                                                                 (10, 11, 5, 'exoneration_demandee', 'true', false, true, 'A soumettre uniquement si l''importateur sollicite une exoneration des droits de douane'),
                                                                                                                                 (11, 12, 4, 'produit_sensible', 'true', false, true, 'Requis pour les biens a double usage ou sensibles necessitant une garantie de destination finale')
ON CONFLICT (id) DO NOTHING;

-- =========================================================
-- regle_documentaire — nouvelles règles (ids 12 à 25)
-- Sources : Arrêté 1675-15 (BO 6366), Décret 2-93-415, Liste des produits soumis à
-- licence d'importation (Ministère de l'Industrie et du Commerce / DGC), ANNEXE_1 (sacs plastiques)
-- =========================================================

INSERT INTO regle_documentaire (id, document_id, reference_juridique_id, dimension, value, obligatoire, active, description) VALUES
(12, 8, 6, 'MODALITE_PAIEMENT', 'Sans paiement', false, true, 'Dispense d''Engagement d''Importation lorsque l''operation est realisee sans paiement (Arrete 1675-15 Art.2)') ON CONFLICT (id) DO NOTHING;

INSERT INTO regle_documentaire (id, document_id, reference_juridique_id, dimension, value, obligatoire, active, description) VALUES
(13, 8, 6, 'REGIME_DOUANIER', 'Regime particulier/suspensif (Titre VI, Chapitre 1er du CDII)', false, true, 'Dispense d''Engagement d''Importation pour les marchandises placees sous un regime douanier particulier (admission temporaire, entrepot, etc.) (Arrete 1675-15 Art.2)') ON CONFLICT (id) DO NOTHING;

INSERT INTO regle_documentaire (id, document_id, reference_juridique_id, dimension, value, obligatoire, active, description) VALUES
(14, 9, NULL, 'CODE_SH', '2524.10.00.00 / 2524.90.00.00', true, true, 'Licence d''importation obligatoire pour l''amiante (Liste DGC des produits soumis a licence)') ON CONFLICT (id) DO NOTHING;

INSERT INTO regle_documentaire (id, document_id, reference_juridique_id, dimension, value, obligatoire, active, description) VALUES
(15, 9, NULL, 'CODE_SH', '3901.10 / 3901.20', true, true, 'Licence d''importation obligatoire pour le polyethylene sous formes primaires (Liste DGC)') ON CONFLICT (id) DO NOTHING;

INSERT INTO regle_documentaire (id, document_id, reference_juridique_id, dimension, value, obligatoire, active, description) VALUES
(16, 9, NULL, 'CODE_SH', '72.04', true, true, 'Licence d''importation obligatoire pour les dechets et debris de fonte, fer ou acier (Liste DGC)') ON CONFLICT (id) DO NOTHING;

INSERT INTO regle_documentaire (id, document_id, reference_juridique_id, dimension, value, obligatoire, active, description) VALUES
(17, 9, NULL, 'CODE_SH', '72071100', true, true, 'Licence d''importation obligatoire pour les billettes d''acier (Liste DGC ; cf. Questionnaire billette d''acier)') ON CONFLICT (id) DO NOTHING;

INSERT INTO regle_documentaire (id, document_id, reference_juridique_id, dimension, value, obligatoire, active, description) VALUES
(18, 9, NULL, 'CODE_SH', '4012.11 a 4012.20', true, true, 'Licence d''importation obligatoire pour les pneumatiques rechapes ou usages (Liste DGC)') ON CONFLICT (id) DO NOTHING;

INSERT INTO regle_documentaire (id, document_id, reference_juridique_id, dimension, value, obligatoire, active, description) VALUES
(19, 9, NULL, 'CODE_SH', '6309.00', true, true, 'Licence d''importation obligatoire pour la friperie (Liste DGC)') ON CONFLICT (id) DO NOTHING;

INSERT INTO regle_documentaire (id, document_id, reference_juridique_id, dimension, value, obligatoire, active, description) VALUES
(20, 9, NULL, 'CODE_SH', '88.06 / EX9503.00', true, true, 'Licence d''importation obligatoire pour les drones / engins volants sans pilote (Liste DGC ; cf. fiche d''information drone)') ON CONFLICT (id) DO NOTHING;

INSERT INTO regle_documentaire (id, document_id, reference_juridique_id, dimension, value, obligatoire, active, description) VALUES
(21, 9, NULL, 'CODE_SH', 'Chapitre 36 sauf 3605.00.00 a 3606.90.00.11', true, true, 'Licence d''importation obligatoire pour les explosifs et articles de pyrotechnie (Liste DGC)') ON CONFLICT (id) DO NOTHING;

INSERT INTO regle_documentaire (id, document_id, reference_juridique_id, dimension, value, obligatoire, active, description) VALUES
(22, 9, NULL, 'CODE_SH', '9307.00.00.00', true, true, 'Licence d''importation obligatoire pour les armes blanches (sabres, epees, baionnettes) (Liste DGC)') ON CONFLICT (id) DO NOTHING;

INSERT INTO regle_documentaire (id, document_id, reference_juridique_id, dimension, value, obligatoire, active, description) VALUES
(23, 9, NULL, 'CODE_SH', '7213.91.90.00', true, true, 'Licence d''importation obligatoire pour les fils machine de fer (Liste DGC, cite Circulaire 6611/311 du 20/11/2024, non rattachable a une reference_juridique existante)') ON CONFLICT (id) DO NOTHING;

INSERT INTO regle_documentaire (id, document_id, reference_juridique_id, dimension, value, obligatoire, active, description) VALUES
(24, 9, 5, 'CATEGORIE_PRODUIT', 'Matieres premieres en polyethylene pour fabrication de sacs plastiques', true, true, 'Licence d''importation obligatoire pour les distributeurs de matieres plastiques destinees a la fabrication de sacs ; decision notifiee sous 30 jours (Decret 2-93-415 Art.4, cite dans le formulaire ANNEXE_1)') ON CONFLICT (id) DO NOTHING;

INSERT INTO regle_documentaire (id, document_id, reference_juridique_id, dimension, value, obligatoire, active, description) VALUES
(25, 11, 5, 'REGIME_DOUANIER', 'Franchise douaniere', true, true, 'Toute demande de franchise douaniere suit un circuit ministeriel, decision motivee sous 30 jours (Decret 2-93-415 Art.4, formulaire DFDMIC)') ON CONFLICT (id) DO NOTHING;

-- Reajustement de la sequence
SELECT setval(pg_get_serial_sequence('regle_documentaire', 'id'), (SELECT MAX(id) FROM regle_documentaire));

-- ---------------------------------------------------------
-- REGLE_COHERENCE
-- ---------------------------------------------------------
INSERT INTO regle_coherence (id, code, libelle, dimension, operateur, valeur_reference, severite, score_penalite, message, active) VALUES
                                                                                                                                       (1, 'RC-001', 'Assurance obligatoire en CIF/CIP', 'assurance', '>', '0', 'critique', 20, 'Le montant assurance doit être supérieur à 0 pour un incoterm CIF ou CIP', true),
                                                                                                                                       (2, 'RC-002', 'Fret/Assurance incohérents en FOB/EXW/FCA', 'fret_assurance', '=', '0', 'warning', 5, 'Un montant de fret ou d''assurance a été détecté alors que l''incoterm ne le prévoit pas', true),
                                                                                                                                       (3, 'RC-003', 'Écart valeur en douane / factures', 'valeur_douane', '<=', '10%', 'critique', 25, 'L''écart entre la valeur en douane et le total des factures dépasse 10%', true),
                                                                                                                                       (4, 'RC-004', 'Devise facture différente du titre', 'devise', '=', 'devise_titre', 'critique', 15, 'La devise de la facture ne correspond pas à la devise du titre d''importation', true),
                                                                                                                                       (5, 'RC-005', 'Poids net supérieur au poids brut', 'poids_net', '<=', 'poids_brut', 'critique', 30, 'Le poids net déclaré est supérieur au poids brut, ce qui est incohérent', true),
                                                                                                                                       (6, 'RC-006', 'Ratio valeur/poids hors bornes SH', 'ratio_valeur_poids', 'entre', 'bornes_sh', 'warning', 10, 'Le ratio valeur en douane / poids net sort des bornes attendues pour ce code SH', true)
ON CONFLICT (id) DO NOTHING;

-- ---------------------------------------------------------
-- Réajustement des séquences (important en PostgreSQL !)
-- ---------------------------------------------------------
SELECT setval(pg_get_serial_sequence('port_entree', 'id'), (SELECT MAX(id) FROM port_entree));
SELECT setval(pg_get_serial_sequence('organisme', 'id'), (SELECT MAX(id) FROM organisme));
SELECT setval(pg_get_serial_sequence('reference_juridique', 'id'), (SELECT MAX(id) FROM reference_juridique));
SELECT setval(pg_get_serial_sequence('document_referentiel', 'id'), (SELECT MAX(id) FROM document_referentiel));
SELECT setval(pg_get_serial_sequence('regle_documentaire', 'id'), (SELECT MAX(id) FROM regle_documentaire));
SELECT setval(pg_get_serial_sequence('regle_coherence', 'id'), (SELECT MAX(id) FROM regle_coherence));