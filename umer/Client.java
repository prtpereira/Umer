
/**
 * Write a description of class Client here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
import java.time.LocalDate;

public class Client extends User implements TemPosicao
{   
    private Ponto2D posicao;
    
    public Client(){
        posicao = new Ponto2D();
    }
    public Client(String email,String nome, String password, String morada,LocalDate data,
                  Ponto2D ponto){
        super(email,nome,password,morada,data);
        this.posicao = ponto;
    }
    public Client( Client c){
        super(c);
        this.posicao = c.getPosicao();
    }
    
    public Ponto2D getPosicao()
    {
        return this.posicao;
    }
    public void setPosicao( Ponto2D x){
        this.posicao = x;
    }    
        
    public Client clone(){
        return new Client(this);
    }
    public String toString(){
        StringBuilder sb = new StringBuilder();
        
        sb.append(super.toString());
        sb.append("\nPosição: ");
        sb.append(posicao.toString());
        
        return sb.toString();
    }
    
    public void addViagem(int index, DataBase db){
        super.addViagem(index);
        Travel t = db.getViagem(index);
        posicao = t.getFim();
    }
    
    public double getGasto(DataBase db){
        double total = 0;
        for( int i : getViagens())
            total += db.getViagem(i).getPrecoReal();
        return total;
    }
}
