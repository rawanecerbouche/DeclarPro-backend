package com.DeclarPro.DeclarPro_backend.service;

import com.DeclarPro.DeclarPro_backend.dto.CalculFraisRequest;
import com.DeclarPro.DeclarPro_backend.dto.CalculFraisResponse;
import com.DeclarPro.DeclarPro_backend.entity.CalculFrais;
import com.DeclarPro.DeclarPro_backend.entity.TitreImportation;
import com.DeclarPro.DeclarPro_backend.repository.CalculFraisRepository;
import com.DeclarPro.DeclarPro_backend.repository.TitreImportationRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

/**
 * Service 5 - Calcul des droits, TVA et frais annexes. Purement calculatoire : les taux sont
 * fournis par l'utilisateur (voir CalculFraisRequest), pas devines a partir du code SH.
 *
 * Formules (base = valeur CIF sauf mention contraire) :
 *  DD   = CIF * droitDouaneTaux
 *  TPI  = CIF * 0.25% (taux fixe)
 *  TIC  = CIF * ticTaux (base non precisee par le cahier des charges pour ce produit generique -
 *         la valeur CIF est utilisee comme base par defaut, taux 0 pour la plupart des produits)
 *  TVA  = (CIF + DD + TIC) * tvaTaux
 *  BMDA = CIF * bmdaTaux ("valeur marchandise" assimilee a la valeur CIF, seule valeur disponible ici)
 *  Total = DD + TPI + TVA + TIC + BMDA
 *
 * When request.titreId() is provided, the result is persisted (CalculFrais) so Service 9's
 * report can show it later; a standalone quick-calc (no titreId) is never persisted.
 */
@Service
public class CalculFraisService {

    private static final BigDecimal TPI_TAUX = new BigDecimal("0.25");
    private static final BigDecimal CENT = BigDecimal.valueOf(100);

    private final CalculFraisRepository calculFraisRepository;
    private final TitreImportationRepository titreImportationRepository;

    public CalculFraisService(CalculFraisRepository calculFraisRepository,
                               TitreImportationRepository titreImportationRepository) {
        this.calculFraisRepository = calculFraisRepository;
        this.titreImportationRepository = titreImportationRepository;
    }

    @Transactional
    public CalculFraisResponse calculer(CalculFraisRequest request) {
        BigDecimal cif = round(resolveCif(request));
        BigDecimal ddTaux = nz(request.droitDouaneTaux());
        BigDecimal tvaTaux = nz(request.tvaTaux());
        BigDecimal ticTaux = nz(request.ticTaux());
        BigDecimal bmdaTaux = nz(request.bmdaTaux());

        BigDecimal montantDD = round(pct(cif, ddTaux));
        BigDecimal montantTPI = round(pct(cif, TPI_TAUX));
        BigDecimal montantTIC = round(pct(cif, ticTaux));
        BigDecimal baseTVA = round(cif.add(montantDD).add(montantTIC));
        BigDecimal montantTVA = round(pct(baseTVA, tvaTaux));
        BigDecimal montantBMDA = round(pct(cif, bmdaTaux));
        BigDecimal total = round(montantDD.add(montantTPI).add(montantTVA).add(montantTIC).add(montantBMDA));

        Long id = null;
        Long titreId = null;
        LocalDate dateCreation = null;

        if (request.titreId() != null) {
            TitreImportation titre = titreImportationRepository.findById(request.titreId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                            "TitreImportation " + request.titreId() + " introuvable"));

            CalculFrais entity = new CalculFrais();
            entity.setTitreImportation(titre);
            entity.setValeurCif(cif);
            entity.setDroitDouaneTaux(ddTaux);
            entity.setMontantDD(montantDD);
            entity.setTpiTaux(TPI_TAUX);
            entity.setMontantTPI(montantTPI);
            entity.setTvaTaux(tvaTaux);
            entity.setBaseTVA(baseTVA);
            entity.setMontantTVA(montantTVA);
            entity.setTicTaux(ticTaux);
            entity.setMontantTIC(montantTIC);
            entity.setBmdaTaux(bmdaTaux);
            entity.setMontantBMDA(montantBMDA);
            entity.setTotalEstime(total);
            entity.setDateCreation(LocalDate.now());

            CalculFrais saved = calculFraisRepository.save(entity);
            id = saved.getId();
            titreId = titre.getId();
            dateCreation = saved.getDateCreation();
        }

        return new CalculFraisResponse(
                id, titreId,
                cif,
                ddTaux, montantDD,
                TPI_TAUX, montantTPI,
                tvaTaux, baseTVA, montantTVA,
                ticTaux, montantTIC,
                bmdaTaux, montantBMDA,
                total,
                dateCreation
        );
    }

    private BigDecimal resolveCif(CalculFraisRequest request) {
        if (request.valeurCif() != null) {
            return request.valeurCif();
        }
        return nz(request.montantFob()).add(nz(request.montantFret())).add(nz(request.montantAssurance()));
    }

    private BigDecimal pct(BigDecimal base, BigDecimal tauxPercent) {
        return base.multiply(tauxPercent).divide(CENT, 6, RoundingMode.HALF_UP);
    }

    private BigDecimal round(BigDecimal v) {
        return v.setScale(2, RoundingMode.HALF_UP);
    }

    private BigDecimal nz(BigDecimal v) {
        return v == null ? BigDecimal.ZERO : v;
    }
}
