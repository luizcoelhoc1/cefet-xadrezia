package xadrezia;

import java.awt.Point;

/**
 * Utilitário para conversão de movimentos em notação SAN e montagem do texto PGN.
 */
public class PgnUtil {

    public static String toSAN(Tabuleiro tabuleiro, Movimento movimento) {
        Peca peca = tabuleiro.getPeca(movimento.getOrigem());
        Peca.TIPO tipo = peca.getTipo();

        // Roque
        if (tipo == Peca.TIPO.REI && Math.abs(movimento.getDestino().x - movimento.getOrigem().x) == 2) {
            return movimento.getDestino().x == 6 ? "O-O" : "O-O-O";
        }

        boolean captura = tabuleiro.getPeca(movimento.getDestino()) != null;
        // Captura en passant: peão anda na diagonal para casa vazia
        if (!captura && tipo == Peca.TIPO.PEAO && movimento.getOrigem().x != movimento.getDestino().x) {
            captura = true;
        }

        String destinoStr = casaToString(movimento.getDestino());

        if (tipo == Peca.TIPO.PEAO) {
            StringBuilder sb = new StringBuilder();
            if (captura) {
                sb.append((char) ('a' + movimento.getOrigem().x)).append("x");
            }
            sb.append(destinoStr);
            if ((movimento.getDestino().y == 0 && peca.getCor() == Peca.COR.BRANCA)
                    || (movimento.getDestino().y == 7 && peca.getCor() == Peca.COR.PRETA)) {
                sb.append("=Q");
            }
            return sb.toString();
        }

        String letra;
        switch (tipo) {
            case CAVALO: letra = "N"; break;
            case BISPO: letra = "B"; break;
            case TORRE: letra = "R"; break;
            case RAINHA: letra = "Q"; break;
            case REI: letra = "K"; break;
            default: letra = "";
        }

        boolean amb = false, ambFile = false, ambRank = false;
        for (Movimento m : tabuleiro) {
            if (m.getOrigem().equals(movimento.getOrigem())) {
                continue;
            }
            if (!m.getDestino().equals(movimento.getDestino())) {
                continue;
            }
            Peca p2 = tabuleiro.getPeca(m.getOrigem());
            if (p2.getTipo() != tipo || p2.getCor() != peca.getCor()) {
                continue;
            }
            amb = true;
            if (m.getOrigem().x == movimento.getOrigem().x) {
                ambFile = true;
            }
            if (m.getOrigem().y == movimento.getOrigem().y) {
                ambRank = true;
            }
        }

        StringBuilder sb = new StringBuilder(letra);
        if (amb) {
            if (!ambFile) {
                sb.append((char) ('a' + movimento.getOrigem().x));
            } else if (!ambRank) {
                sb.append(8 - movimento.getOrigem().y);
            } else {
                sb.append((char) ('a' + movimento.getOrigem().x)).append(8 - movimento.getOrigem().y);
            }
        }
        if (captura) {
            sb.append("x");
        }
        sb.append(destinoStr);
        return sb.toString();
    }

    public static String casaToString(Point p) {
        return "" + (char) ('a' + p.x) + (8 - p.y);
    }

    /**
     * @param sanMoves lances já convertidos para SAN, em ordem
     * @param resultado "1-0", "0-1" ou "1/2-1/2"
     * @param brancas nome/identificação do motor das brancas
     * @param pretas nome/identificação do motor das pretas
     * @return texto completo do PGN, incluindo cabeçalho
     */
    public static String montarPgn(Iterable<String> sanMoves, String resultado, String brancas, String pretas) {
        StringBuilder pgn = new StringBuilder();
        pgn.append("[Event \"XadrezIA self-play\"]\n");
        pgn.append("[Site \"local\"]\n");
        pgn.append("[Date \"????.??.??\"]\n");
        pgn.append("[Round \"1\"]\n");
        pgn.append("[White \"").append(brancas).append("\"]\n");
        pgn.append("[Black \"").append(pretas).append("\"]\n");
        pgn.append("[Result \"").append(resultado).append("\"]\n\n");

        int i = 0;
        for (String san : sanMoves) {
            if (i % 2 == 0) {
                pgn.append((i / 2 + 1)).append(". ");
            }
            pgn.append(san).append(" ");
            i++;
        }
        pgn.append(resultado).append("\n");
        return pgn.toString();
    }
}
