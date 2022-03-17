package Assignment1.Assignment1Skeleton.src.main.java;

import java.io.*;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.ArrayList;
import java.util.HashMap;


public class QuarantineSystem {
    public static class DashBoard {
    	Map<String, Person> People;
        List<Integer> patientNums;
        List<Integer> infectNums;
        List<Double> infectAvgNums;
        List<Integer> vacNums;
        List<Integer> vacInfectNums;

        public DashBoard(Map<String, Person> p_People) {
            this.People = p_People;
            this.patientNums = new ArrayList<>(8);
            this.infectNums = new ArrayList<>(8);
            this.infectAvgNums = new ArrayList<>(8);
            this.vacNums = new ArrayList<>(8);
            this.vacInfectNums = new ArrayList<>(8);
        }

        public void runDashBoard() {
            for (int i = 0; i < 8; i++) {
                this.patientNums.add(0);
                this.infectNums.add(0);
                this.vacNums.add(0);
                this.vacInfectNums.add(0);
            }
            
            for(Entry<String,Person> peopleSet:this.People.entrySet()) {
            	
            	Person p=peopleSet.getValue();
            	int ageIndex=Math.min(p.getAge()/10,7);
            	patientNums.set(ageIndex, patientNums.get(ageIndex)+Math.min(p.getInfectCnt(),1));
            	infectNums.set(ageIndex, infectNums.get(ageIndex)+p.getInfectCnt());
            	
            	if(p.getIsVac()) {
        			vacNums.set(ageIndex, vacNums.get(ageIndex)+1);
        			vacInfectNums.set(ageIndex, vacInfectNums.get(ageIndex)+Math.min(p.getInfectCnt(),1));
        		}
            }
            
            for (int i = 0; i < 8; i++) {
                this.infectAvgNums.add((patientNums.get(i)==0)?0:((double)infectNums.get(i))/((double)patientNums.get(i)));
            }
            
        }
    }


    private Map<String,Person> People;
    private Map<String,Patient> Patients;

    private List<Record> Records;
    private Integer newHospitalNum;
    private Map<String,Hospital> Hospitals;

    private DashBoard dashBoard;

    public QuarantineSystem() throws IOException {
        importPeople();
        importHospital();
        importRecords();
        dashBoard = null;
        Patients = new HashMap<>();
    }

    public void startQuarantine() throws IOException {
        /*
         * Task 1: Saving Patients
         */
        System.out.println("Task 1: Saving Patients");
        
        for(Record r:Records) {
        	switch(r.getStatus()) {
        		case Confirmed:
        			saveSinglePatient(r);
        			break;
        		case Recovered:
        			releaseSinglePatient(r);
        			break;
        		default:
        			break;
        	}
        }
        
        exportRecordTreatment();

        /*
         * Task 2: Displaying Statistics
         */
        System.out.println("Task 2: Displaying Statistics");
        dashBoard = new DashBoard(this.People);
        dashBoard.runDashBoard();
        exportDashBoard();
    }

    /*
     * Save a single patient when the status of the record is Confirmed
     */
    public void saveSinglePatient(Record r) {
    	Person p = People.get(r.getIDCardNo());
    	Patient patient = Patients.get(r.getIDCardNo());
		// already confirmed
		if(patient!=null) {
			patient.setSymptomLevel(r.getSymptomLevel());
			r.setHospitalID(patient.getHospitalID());
		}
		// recovered or not yet confirmed
		else{
			Map<Integer, Hospital> distanceMap = new HashMap<>();
			
			p.setInfectCnt(p.getInfectCnt()+1);
			
			patient = new Patient(p,r.getSymptomLevel());
			Patients.put(p.getIDCardNo(),patient); 
			//Get the hospital distance
			for(Entry<String,Hospital> hospitalSet:Hospitals.entrySet()) {
				Hospital h = hospitalSet.getValue();
				distanceMap.put(h.getLoc().getDisSquare(p.getLoc()), h);
			}
			//Sorting
			List<Entry<Integer,Hospital>> distanceMapList = new ArrayList<>(distanceMap.entrySet());
			distanceMapList.sort(Entry.comparingByKey());
			//Finding hospital with enough capacities
			String assignedID=null;
			for(int i=0;i<distanceMapList.size();i++) {
				Hospital h=distanceMapList.get(i).getValue();
				if(h.getCapacity().getSingleCapacity(r.getSymptomLevel())>0) {
					assignedID=h.HospitalID;
					h.addPatient(patient);
					break;
				}
			}
			//Using old hospital
			if(assignedID!=null) {
    			r.setHospitalID(assignedID);
			}
			//Constructing new hospital
			else {
				newHospitalNum++;
				assignedID="H-New-"+newHospitalNum.toString();
				Hospital hospital= new Hospital(assignedID,p.getLoc());
				Hospitals.put(assignedID, hospital);
    			r.setHospitalID(assignedID);
    			hospital.addPatient(patient);
			}
			patient.setHospitalID(assignedID);
		}
    }

    /*
     * Release a single patient when the status of the record is Recovered
     */
    public void releaseSinglePatient(Record r) {
    	Patient patient=Patients.get(r.getIDCardNo());
		Hospital h=Hospitals.get(patient.getHospitalID());
		r.setHospitalID(h.HospitalID);
		h.releasePatient(patient);
		Patients.remove(patient.getIDCardNo());
		if(h.getPatients(SymptomLevel.Critical)==null && h.getPatients(SymptomLevel.Mild)==null && h.getPatients(SymptomLevel.Moderate)==null) {
			if(h.HospitalID.contains("h-New-")) {
				Hospitals.remove(h.HospitalID);
			}
		}
    }

    /*
     * Import the information of the people in the area from Person.txt
     * The data is finally stored in the attribute People
     * You do not need to change the method.
     */
    public void importPeople() throws IOException {
        this.People = new HashMap<>();
        File filename = new File("A1/Assignment1/Assignment1Skeleton/sampleData/sample3/data/Person.txt");
        InputStreamReader reader = new InputStreamReader(new FileInputStream(filename));
        BufferedReader br = new BufferedReader(reader);
        String line = br.readLine();
        int lineNum = 0;

        while (line != null) {
            lineNum++;
            if (lineNum > 1) {
                String[] records = line.split("        ");
                assert (records.length == 6);
                String pIDCardNo = records[0];
                System.out.println(pIDCardNo);
                int XLoc = Integer.parseInt(records[1]);
                int YLoc = Integer.parseInt(records[2]);
                Location pLoc = new Location(XLoc, YLoc);
                assert (records[3].equals("Male") || records[3].equals("Female"));
                String pGender = records[3];
                int pAge = Integer.parseInt(records[4]);
                assert (records[5].equals("Yes") || records[5].equals("No"));
                boolean pIsVac = (records[5].equals("Yes"));
                Person p = new Person(pIDCardNo, pLoc, pGender, pAge, pIsVac);
                this.People.put(pIDCardNo,p);
            }
            line = br.readLine();
        }
        br.close();
    }

    /*
     * Import the information of the records
     * The data is finally stored in the attribute Records
     * You do not need to change the method.
     */
    public void importRecords() throws IOException {
        this.Records = new ArrayList<>();

        File filename = new File("A1/Assignment1/Assignment1Skeleton/sampleData/sample3/data/Record.txt");
        InputStreamReader reader = new InputStreamReader(new FileInputStream(filename));
        BufferedReader br = new BufferedReader(reader);
        String line = br.readLine();
        int lineNum = 0;

        while (line != null) {
            lineNum++;
            if (lineNum > 1) {
                String[] records = line.split("        ");
                assert(records.length == 3);
                String pIDCardNo = records[0];
                System.out.println(pIDCardNo);
                assert(records[1].equals("Critical") || records[1].equals("Moderate") || records[1].equals("Mild"));
                assert(records[2].equals("Confirmed") || records[2].equals("Recovered"));
                Record r = new Record(pIDCardNo, records[1], records[2]);
                Records.add(r);
            }
            line = br.readLine();
        }
        br.close();
    }

    /*
     * Import the information of the hospitals
     * The data is finally stored in the attribute Hospitals
     * You do not need to change the method.
     */
    public void importHospital() throws IOException {
        this.Hospitals = new HashMap<>();
        this.newHospitalNum = 0;

        File filename = new File("A1/Assignment1/Assignment1Skeleton/sampleData/sample3/data/Hospital.txt");
        InputStreamReader reader = new InputStreamReader(new FileInputStream(filename));
        BufferedReader br = new BufferedReader(reader);
        String line = br.readLine();
        int lineNum = 0;

        while (line != null) {
            lineNum++;
            if (lineNum > 1) {
                String[] records = line.split("        ");
                assert(records.length == 6);
                String pHospitalID = records[0];
                System.out.println(pHospitalID);
                int XLoc = Integer.parseInt(records[1]);
                int YLoc = Integer.parseInt(records[2]);
                Location pLoc = new Location(XLoc, YLoc);
                int pCritialCapacity = Integer.parseInt(records[3]);
                int pModerateCapacity = Integer.parseInt(records[4]);
                int pMildCapacity = Integer.parseInt(records[5]);
                Capacity cap = new Capacity(pCritialCapacity, pModerateCapacity, pMildCapacity);
                Hospital hospital = new Hospital(pHospitalID, pLoc, cap);
                this.Hospitals.put(pHospitalID,hospital);
            }
            line = br.readLine();
        }
        br.close();
    }

    /*
     * Export the information of the records
     * The data is finally dumped into RecordTreatment.txt
     * DO NOT change the functionality of the method
     * Otherwise, you may generate wrong results in Task 1
     */
    public void exportRecordTreatment() throws IOException {
        File filename = new File("A1/Assignment1/Assignment1Skeleton/sampleData/sample3/output/RecordTreatment.txt");
        OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(filename));
        BufferedWriter bw = new BufferedWriter(writer);
        bw.write("IDCardNo        SymptomLevel        Status        HospitalID\n");
        for (Record record : Records) {
            //Invoke the toString method of Record.
            bw.write(record.toString() + "\n");
        }
        bw.close();
    }

    /*
     * Export the information of the dashboard
     * The data is finally dumped into Statistics.txt
     * DO NOT change the functionality of the method
     * Otherwise, you may generate wrong results in Task 2
     */
    public void exportDashBoard() throws IOException {
        File filename = new File("A1/Assignment1/Assignment1Skeleton/sampleData/sample3/output/Statistics.txt");
        OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(filename));
        BufferedWriter bw = new BufferedWriter(writer);

        bw.write("AgeRange        patientNums        infectAvgNums        vacNums        vacInfectNums\n");

        for (int i = 0; i < 8; i++) {
            String ageRageStr = "";
            switch (i) {
                case 0:
                    ageRageStr = "(0, 10)";
                    break;
                case 7:
                    ageRageStr = "[70, infinite)";
                    break;
                default:
                    ageRageStr = "[" + String.valueOf(i) + "0, " + String.valueOf(i + 1) + "0)";
                    break;
            }
            String patientNumStr = String.valueOf(dashBoard.patientNums.get(i));
            String infectAvgNumsStr = String.valueOf(dashBoard.infectAvgNums.get(i));
            String vacNumsStr = String.valueOf(dashBoard.vacNums.get(i));
            String vacInfectNumsStr = String.valueOf(dashBoard.vacInfectNums.get(i));

            bw.write(ageRageStr + "        " + patientNumStr + "        " + infectAvgNumsStr + "        " + vacNumsStr + "        " + vacInfectNumsStr + "\n");
        }

        bw.close();
    }

    /* The entry of the project */
    public static void main(String[] args) throws IOException {
        QuarantineSystem system = new QuarantineSystem();
        System.out.println("Start Quarantine System");
        system.startQuarantine();
        System.out.println("Quarantine Finished");
    }
}
