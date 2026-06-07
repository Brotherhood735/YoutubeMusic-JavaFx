package com.unimusic.Model.Repository.Implementation;

import com.unimusic.Model.Repository.UniComment;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CommentRepositoryImplTest {

    private static final String TEST_CSV_PATH = "src/test/resources/comments_test.csv";
    private static final String TEST_HEADER = "id,mediaId,parentId,username,text,depth";

    @Test
    public void testParseLine_CsvValido_CaricamentoCorretto() {
        CommentRepositoryImpl repository = new CommentRepositoryImpl(TEST_CSV_PATH, TEST_HEADER);
        String lineaCsvValida = "1,100,-1,MarioRossi,traccia,0";

        UniComment commentoCaricato = repository.parseLine(lineaCsvValida);

        assertNotNull(commentoCaricato, "Il commento non dovrebbe essere nullo con un CSV valido.");
        assertEquals(1, commentoCaricato.getId());
        assertEquals("MarioRossi", commentoCaricato.getUsername());
    }

    @Test
    public void testParseLine_CsvCorrotto_RitornaNull() {
        CommentRepositoryImpl repository = new CommentRepositoryImpl(TEST_CSV_PATH, TEST_HEADER);

        String lineaCsvCorrotta = "Kiaro,100,-1,MarioRossi,Test di testo,0";

        UniComment commentoCaricato = repository.parseLine(lineaCsvCorrotta);

        assertNull(commentoCaricato, "Il metodo dovrebbe ritornare null se i dati numerici sono corrotti.");
    }
}