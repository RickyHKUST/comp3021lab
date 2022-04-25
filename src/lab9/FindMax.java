package lab9;

/**
 * 
 * COMP 3021
 * 
This is a class that prints the maximum value of a given array of 90 elements

This is a single threaded version.

Create a multi-thread version with 3 threads:

one thread finds the max among the cells [0,29] 
another thread the max among the cells [30,59] 
another thread the max among the cells [60,89]

Compare the results of the three threads and print at console the max value.

 * 
 * @author valerio
 *
 */
public class FindMax {
	// this is an array of 90 elements
	// the max value of this array is 9999
	static int[] array = { 1, 34, 5, 6, 343, 5, 63, 5, 34, 2, 78, 2, 3, 4, 5, 234, 678, 543, 45, 67, 43, 2, 3, 4543,
			234, 3, 454, 1, 2, 3, 1, 9999, 34, 5, 6, 343, 5, 63, 5, 34, 2, 78, 2, 3, 4, 5, 234, 678, 543, 45, 67, 43, 2,
			3, 4543, 234, 3, 454, 1, 2, 3, 1, 34, 5, 6, 5, 63, 5, 34, 2, 78, 2, 3, 4, 5, 234, 678, 543, 45, 67, 43, 2,
			3, 4543, 234, 3, 454, 1, 2, 3 };

	public static void main(String[] args) throws InterruptedException {
		new FindMax().printMax();
	}

	public void printMax() throws InterruptedException {
		// this is a single threaded version
		LocalMax lm1 = new LocalMax(0,29,array);
		LocalMax lm2 = new LocalMax(30,59,array);
		LocalMax lm3 = new LocalMax(60,89,array);
		Thread findMax1 = new Thread(lm1);
		Thread findMax2 = new Thread(lm2);
		Thread findMax3 = new Thread(lm3);
		findMax1.start();
		findMax2.start();
		findMax3.start();
		findMax1.join();
		findMax2.join();
		findMax3.join();
		int max = Math.max(lm1.max, Math.max(lm2.max,lm3.max));
		System.out.println("the max value is " + max);
	}
	
	class LocalMax implements Runnable{
		int[] array;
		int begin;
		int end;
		int max;
		
		LocalMax(int begin,int end,int[] array){
			this.begin=begin;
			this.end=end;
			this.array=array;
		}

		@Override
		public void run() {
			// you should NOT change this function
			max = array[begin];
			for (int i = begin + 1; i <= end; i++) {
				if (array[i] > max) {
					max = array[i];
				}
			}
		}
	}

}
