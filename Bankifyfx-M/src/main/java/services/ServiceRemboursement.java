package services;

import models.Credit;
import models.Remboursement;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import utils.MyDatabase;
import java.text.SimpleDateFormat;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class ServiceRemboursement implements IService1<Remboursement> {
    Connection connection;

    public ServiceRemboursement() {
        connection = MyDatabase.getInstance().getConnection();
    }

    @Override
    public void add(Remboursement remboursement) throws SQLException {
        String req = "INSERT INTO remboursement (duree_restante, montant_r, montant_restant, date_r, credit_id) " +
                "VALUES (?, ?, ?, ?, ?)";

        PreparedStatement preparedStatement = connection.prepareStatement(req);
        preparedStatement.setInt(1, remboursement.getDureeRestante());
        preparedStatement.setDouble(2, remboursement.getMontantR());
        preparedStatement.setDouble(3, remboursement.getMontantRestant());
        preparedStatement.setDate(4, new java.sql.Date(remboursement.getDateR().getTime()));
        preparedStatement.setInt(5, remboursement.getCredit().getId());

        preparedStatement.executeUpdate();
        System.out.println("Remboursement ajouté");
    }

    @Override
    public void edit(Remboursement remboursement) throws SQLException {
        String req = "UPDATE remboursement SET duree_restante=?, montant_r=?, montant_restant=?, date_r=?, credit_id=? WHERE id=?";

        PreparedStatement preparedStatement = connection.prepareStatement(req);
        preparedStatement.setInt(1, remboursement.getDureeRestante());
        preparedStatement.setDouble(2, remboursement.getMontantR());
        preparedStatement.setDouble(3, remboursement.getMontantRestant());
        preparedStatement.setDate(4, new java.sql.Date(remboursement.getDateR().getTime()));
        preparedStatement.setInt(5, remboursement.getCredit().getId());
        preparedStatement.setInt(6, remboursement.getId());

        preparedStatement.executeUpdate();
        System.out.println("Remboursement modifié");
    }

    @Override
    public void delete(int id) throws SQLException {
        String req = "DELETE FROM remboursement WHERE id=?";

        PreparedStatement preparedStatement = connection.prepareStatement(req);
        preparedStatement.setInt(1, id);

        preparedStatement.executeUpdate();
        System.out.println("Remboursement supprimé");
    }

    @Override
    public List<Remboursement> getAll() throws SQLException {
        List<Remboursement> remboursements = new ArrayList<>();
        String req = "SELECT * FROM remboursement";

        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(req);

        while (rs.next()) {
            int id = rs.getInt("id");
            int dureeRestante = rs.getInt("duree_restante");
            double montantR = rs.getDouble("montant_r");
            double montantRestant = rs.getDouble("montant_restant");
            Date dateR = rs.getDate("date_r");

            int creditId = rs.getInt("credit_id");
            Credit credit = new ServiceCredit().getById(creditId);

            Remboursement remboursement = new Remboursement(id, dureeRestante, montantR, montantRestant, dateR, credit);
            remboursements.add(remboursement);
        }

        return remboursements;
    }

    @Override
    public Remboursement getById(int id) throws SQLException {
        String req = "SELECT * FROM remboursement WHERE id=?";

        PreparedStatement preparedStatement = connection.prepareStatement(req);
        preparedStatement.setInt(1, id);

        ResultSet rs = preparedStatement.executeQuery();

        if (rs.next()) {
            int dureeRestante = rs.getInt("duree_restante");
            double montantR = rs.getDouble("montant_r");
            double montantRestant = rs.getDouble("montant_restant");
            Date dateR = rs.getDate("date_r");

            int creditId = rs.getInt("credit_id");
            Credit credit = new ServiceCredit().getById(creditId);

            return new Remboursement(id, dureeRestante, montantR, montantRestant, dateR, credit);
        } else {
            return null;
        }
    }

    public List<Remboursement> getByCredit(int creditId) throws SQLException {
        List<Remboursement> remboursements = new ArrayList<>();
        String req = "SELECT * FROM remboursement WHERE credit_id="+creditId+";";
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(req);
        while (rs.next()) {
            int id = rs.getInt("id");
            int dureeRestante = rs.getInt("duree_restante");
            double montantR = rs.getDouble("montant_r");
            double montantRestant = rs.getDouble("montant_restant");
            Date dateR = rs.getDate("date_r");
            Credit credit = new ServiceCredit().getById(creditId);
            Remboursement remboursement = new Remboursement(id, dureeRestante, montantR, montantRestant, dateR, credit);
            remboursements.add(remboursement);
        }
        return remboursements;
    }

    public void sendSms(Remboursement remboursement) {
        Twilio.init("AC3c608f407fd3e259afc11997317f4308","aca04423a2941fdb0319d45f7c67d357");
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String msg = "Succés de remboursement\nDe montant "+remboursement.getMontantR()+" DT\nRéalisé le "+dateFormat.format(remboursement.getDateR())+"\nIl vous reste "+remboursement.getMontantRestant()+" DT a payé sur les prochaines "+remboursement.getDureeRestante()+" mois";
        Message message = Message.creator(
                        new PhoneNumber(remboursement.getCredit().getCompte().getTel()),
                        new PhoneNumber("+14243734278"),
                        msg)
                .create();
        System.out.println("Message envoyé");
    }

    public void generatePdf(Remboursement remboursement){
        try {
            PDDocument document = new PDDocument();
            PDPage page = new PDPage();
            PDRectangle pageSize = page.getMediaBox();
            document.addPage(page);
            String title="Réçu de remboursement";
            try (PDPageContentStream contentStream = new PDPageContentStream(document, page, PDPageContentStream.AppendMode.OVERWRITE, true)) {
                contentStream.setNonStrokingColor(new Color(128, 0, 128));
                contentStream.addRect(0, 0, pageSize.getWidth(), pageSize.getHeight());
                contentStream.fill();
                float titleFontSize = 32;
                float titleWidth = PDType1Font.HELVETICA_BOLD.getStringWidth(title) / 1000 * titleFontSize;
                float titleX = (pageSize.getWidth() - titleWidth) / 2;
                contentStream.setNonStrokingColor(new Color(207, 135, 196));
                contentStream.beginText();
                contentStream.setFont(PDType1Font.HELVETICA_BOLD, titleFontSize);
                contentStream.newLineAtOffset(titleX, 730);
                contentStream.showText(title);
                contentStream.endText();

                float boxWidth = 500;
                float boxHeight = 200;
                float boxX =60;
                float boxY = 480;
                float cornerRadius = 15;
                contentStream.setNonStrokingColor(Color.WHITE);
                contentStream.moveTo(boxX + cornerRadius, boxY);
                contentStream.lineTo(boxX + boxWidth - cornerRadius, boxY);
                contentStream.curveTo(boxX + boxWidth - cornerRadius / 2, boxY, boxX + boxWidth, boxY + cornerRadius / 2, boxX + boxWidth, boxY + cornerRadius);
                contentStream.lineTo(boxX + boxWidth, boxY + boxHeight - cornerRadius);
                contentStream.curveTo(boxX + boxWidth, boxY + boxHeight - cornerRadius / 2, boxX + boxWidth - cornerRadius / 2, boxY + boxHeight, boxX + boxWidth - cornerRadius, boxY + boxHeight);
                contentStream.lineTo(boxX + cornerRadius, boxY + boxHeight);
                contentStream.curveTo(boxX + cornerRadius / 2, boxY + boxHeight, boxX, boxY + boxHeight - cornerRadius / 2, boxX, boxY + boxHeight - cornerRadius);
                contentStream.lineTo(boxX, boxY + cornerRadius);
                contentStream.curveTo(boxX, boxY + cornerRadius / 2, boxX + cornerRadius / 2, boxY, boxX + cornerRadius, boxY);
                contentStream.closeAndFillAndStroke();

                contentStream.setNonStrokingColor(new Color(128, 0, 128));
                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
                contentStream.beginText();
                contentStream.newLineAtOffset(165, 625);
                contentStream.showText("Numéro de Remboursement: ");
                contentStream.newLineAtOffset(0, -20);
                contentStream.showText("Numéro de Crédit: ");
                contentStream.newLineAtOffset(0, -20);
                contentStream.showText("Montant Remboursé: ");
                contentStream.newLineAtOffset(0, -20);
                contentStream.showText("Montant Restant: ");
                contentStream.newLineAtOffset(0, -20);
                contentStream.showText("Date Remboursement: ");
                contentStream.newLineAtOffset(0, -20);
                contentStream.showText("Durée Restante: ");
                contentStream.endText();

                contentStream.setNonStrokingColor(Color.BLACK);
                contentStream.setFont(PDType1Font.HELVETICA, 10);
                contentStream.beginText();
                contentStream.newLineAtOffset(395, 625);
                contentStream.showText(String.valueOf(remboursement.getId()));
                contentStream.newLineAtOffset(0, -20);
                contentStream.showText(String.valueOf(remboursement.getCredit().getId()));
                contentStream.newLineAtOffset(0, -20);
                contentStream.showText(String.valueOf(remboursement.getMontantR()));
                contentStream.newLineAtOffset(0, -20);
                contentStream.showText(String.valueOf(remboursement.getMontantRestant()));
                contentStream.newLineAtOffset(0, -20);
                contentStream.showText(String.valueOf(remboursement.getDateR()));
                contentStream.newLineAtOffset(0, -20);
                contentStream.showText(String.valueOf(remboursement.getDureeRestante()));
                contentStream.endText();

                contentStream.setNonStrokingColor(new Color(207, 135, 196));
                contentStream.beginText();
                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
                contentStream.newLineAtOffset(510, 440);
                contentStream.showText("Signer");
                contentStream.endText();
            }
            document.save("remboursement_"+remboursement.getId()+".pdf");
            document.close();
            System.out.println("Pdf généré");
            File file = new File("remboursement_"+remboursement.getId()+".pdf");
            Desktop.getDesktop().open(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}