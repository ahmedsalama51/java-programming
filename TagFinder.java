/**
 * Finds a protein within a strand of DNA represented as a string of c,g,t,a letters.
 * A protein is a part of the DNA strand marked by start and stop codons (DNA triples)
 * that is a multiple of 3 letters long.
 *
 * @author Duke Software Team 
 */
import edu.duke.*;
import java.io.*;

public class TagFinder {
    
    public static float cgRatio(String dna) {

        dna = dna.toLowerCase();

        int dnaLen = dna.length();
        int gCount = countLetterInWord('g', dna);
        int cCount = countLetterInWord('c', dna);
        return (float) (gCount + cCount) / dnaLen;

    }
    /**
     * Count number of a letter in a word
     *
     * @param letter
     * @param word
     * @return
     */
    public static int countLetterInWord(char letter, String word) {

        int counter = 0;

        for (int i = 0; i < word.length(); i++) {

            if (word.charAt(i) == letter) {
                counter++;
            }

        }

        return counter;

    }
    
    public int findStopCodeon(String dna_code,int start,String stop)
    {
        int currIndex = dna_code.indexOf(stop,start+3);
        while(currIndex != -1){
            int diff = currIndex - start;
            if (diff % 3 == 0)
            {
                return currIndex;
            }
            else{
                currIndex = dna_code.indexOf(stop,currIndex+1);
            }
            
        }
        return dna_code.length();
        
    }
    public String findGene(String dna,int start) {
        //int start = dna.indexOf("atg");
        if (start == -1) {
            return "end";
        }
        //System.out.println("Start ---> " + start);
        int TAA_code = findStopCodeon(dna,start,"taa");
        //System.out.println("TAA_code ---> " + TAA_code);
        int TAG_code = findStopCodeon(dna,start,"tag");
        //System.out.println("TAG_code ---> " + TAG_code);
        int TGA_code = findStopCodeon(dna,start,"tga");
        //System.out.println("TGA_code ---> " + TGA_code);
        int minIndx = 0;
        if (TAA_code == -1 ||
                (TGA_code != -1 && TGA_code < TAA_code))
        {
            minIndx = TGA_code;
        }
        else{
            minIndx = TAA_code;
        }
        if (minIndx == -1 ||
                (TAG_code != -1 && TAG_code < minIndx))
        {
            minIndx = TAG_code;
        }
        if(minIndx == dna.length()){
            return "end";
          }
        String gene = dna.substring(start,minIndx+3);
        //System.out.println("Found ---> " + gene);
        return gene;
    }
    /**
     * Count the codon CTG in a strand of DNA
     *
     * @param dna is a strand of DNA
     * @return count of CTG
     */
    public static int countCTGCodon(String dna) {
        dna = dna.toLowerCase();
        int count = dna.length() - dna.replace("ctg", "").length();
        System.out.println("CTG Count: " + count / 3);
        return count;
    }
    public void findAllGense(String dna) {
        dna = dna.toLowerCase();
        int startIndex = 0;
        int endPos = 0;
        int count = 0;
        int over_60 = 0;
        int genCount = 0;
        int longest = 0;
        while(true){
            startIndex = dna.indexOf("atg", endPos);
            String CurrentGene = findGene(dna,startIndex);
            if (CurrentGene == "end"){
                System.out.println("------------------- END -----------------");
                break;
            }
            endPos = dna.indexOf(CurrentGene,startIndex)+
                        CurrentGene.length();
            count++;
            if(CurrentGene.length() > 60)
            {
                over_60++;
            }
            if(CurrentGene.length() > longest)
            {
                longest = CurrentGene.length();
            }
            if (cgRatio(CurrentGene) > 0.35) {
                System.out.println("C-G Ration: " + cgRatio(CurrentGene));
                genCount++;
            }
            System.out.println("-------------------\nfound Gene " + CurrentGene + ".\nLength " + CurrentGene.length());
           }
        System.out.println("Count: " + count);
        System.out.println("Countover 60: " + over_60);
        System.out.println("Longest gene:: " + longest);
        System.out.println("C-G Count:: " + genCount);
       }
    


    public void realTesting() {
        //                 012345678901234567890123   
        //findAllGense("aaaatggctattggattaagaacatagg");
        //countCTGCodon("atggctattggattaagaaatagg");
        DirectoryResource dr = new DirectoryResource();
        for (File f : dr.selectedFiles()) {
            FileResource fr = new FileResource(f);
            String s = fr.asString();
            System.out.println("Read  characters: "+ s.length());
            findAllGense(s);
            countCTGCodon(s);
        }
    }
}
