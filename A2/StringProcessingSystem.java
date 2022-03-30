import java.util.Scanner;

public class StringProcessingSystem {
    private static String originalText;
    private static String modifiedText;  //It is not compulsory to update this field. We just create it for you to store the temporary result for the task 2,3, and 5.
    private static Scanner reader=new Scanner(System.in); //static reader, no need to open reader in the methods of the class

    public static void split() {
        System.out.print("Please input a delimiter: ");
        String target = "";
        target = reader.next();

        // TODO 1: Split the string whenever there is a delimiter
        //         Output each substring in separate line
        String[] outputs = originalText.split(target);
        for(String s:outputs) {
        	System.out.println(s);
        }
    }

    public static void removeSubstring() {
        System.out.print("Please input string to remove: ");
        String target = reader.next();

        System.out.println("String before removing \'" + target + "\': " + originalText);

        // TODO 2: Remove the target character sequence from the original string
        //         if it doesn't exist in the string, output "target is not found"
        //         e.g., original = "Hello World", target = "Wor"
        //               target = "Hello ld"

        modifiedText = "";
        int targetIndex=0,textIndex=0;
        while(textIndex<originalText.length()) {
        	if(targetIndex<target.length() && target.charAt(targetIndex)==originalText.charAt(textIndex))
        		targetIndex++;
        	else
        		modifiedText+=originalText.charAt(textIndex);
    		textIndex++;
        }
        if(modifiedText.equals(originalText))
        	System.out.println("target is not found");
        else
        	System.out.println("String after removing \'" + target + "\': " + modifiedText);
    }


    public static void shiftString() {
        System.out.print("Please input amount of shift: ");
        int shiftAmount = reader.nextInt();

        // TODO 3: Shift characters to the right by the amount specified by shiftAmount
        //         e.g., Input string: "Hello World"
        //               shiftAmount: 3
        //               Result: "rldHello Wo"
        int length = originalText.length();
        int shift=(shiftAmount%length + length)%length;
    	modifiedText=originalText.substring(length-shift,length) + originalText.substring(0,length-shift);
        System.out.println("After shifting \"" + originalText + "\" by "+shiftAmount+": " + modifiedText);
    }


    public static void countVowels() {
        // TODO 4: Count number of vowels in String. (A, E, I, O, U, a, e, i, o, and u)
    	int counter=0;
    	String vowels = "AEIOUaeiou";
    	for(char s:originalText.toCharArray()) {
    		if(vowels.indexOf(s)!=-1) {counter++;}
    	}
    	System.out.println("number of vowels in \"" + originalText + "\": "+counter);
    }

    public static void ceaserCipher() {
        System.out.print("Please input amount of shift: ");
        //Scanner reader = new Scanner(System.in); // Scanner is used for Java input
        int shift = reader.nextInt();

        // TODO 5: Rotate each characters forward by the amount specified by shiftAmount. Any numbers and special characters should be left the same.
        //         Also, The string should be converted into capital letters first
        //         e.g., Input string: "Hello World! 123"
        //               shiftAmount: 30
        //               Result: "LIPPS ASVPH! 123"

        modifiedText="";
        shift=(shift%26+26)%26;
        for(char s:originalText.toCharArray()) {
        	if(s<='z' && s>='a') {s=(char) (s+'A'-'a');}
        	if(s<='Z' && s>='A') {
        		s=(char)(s+shift);
        		if (s>='Z') s-=26;
        	}
        	modifiedText+=s;
        }
        System.out.println("ciphertext: " + modifiedText);
    }

    public static void main(String args[]){

        System.out.println("Welcome to the String Handling System!");
        System.out.print("Please input a string you want to process: ");

        Scanner reader = new Scanner(System.in); // Scanner is used for Java input
        originalText = reader.nextLine();

        String option = "";

        while (!option.equals("Q")) {
            System.out.println("=========== Options ============");
            System.out.println("1: Split the string");
            System.out.println("2: Remove all substring from string");
            System.out.println("3: Shift the string");
            System.out.println("4: Count number of vowels");
            System.out.println("5: Ceaser cipher");
            System.out.println("================================");
            System.out.println("Please choose an option (type in Q if you want to quit): ");
            option = reader.next();

            switch (option){
                case "1":
                    split();
                    break;
                case "2":
                    removeSubstring();
                    break;
                case "3":
                    shiftString();
                    break;
                case "4":
                    countVowels();
                    break;
                case "5":
                    ceaserCipher();
                    break;
                default:
                    if (option.equals("Q"))
                        System.out.println("Goodbye!");
                    else
                        System.out.println("Invalid Option! Please try again: ");
                    break;
            }
        }
        reader.close();
    }
}