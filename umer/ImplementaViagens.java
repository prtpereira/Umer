
/**
 * Write a description of interface ImplementaViagens here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
import java.util.List;
import java.time.LocalDate;

public interface ImplementaViagens 
{
    // adiciona uma viagem ( cuja chave na db é index)
    void addViagem(int index, DataBase db);
    // retorna a lista das chaves (index) das viagens na sua DataBase
    List<Integer> getViagens();
    //retorna a chave da viagem numero i que o fez
    int getViagem(int i);
    //retorn o numero de viagens
    public int numDeViagens();
    //retorna uma lista com o ponto de partida e chegada de cada viagem que realizou
    public List<String> listaViagens(DataBase db);
    //retorn uma lista de vigens entre as datas
    public List<String> listaViagens(DataBase db, LocalDate inicio, LocalDate fim);
}
