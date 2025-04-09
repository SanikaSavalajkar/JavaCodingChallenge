package main;

import dao.HospitalServiceImpl;
import dao.IHospitalService;
import entity.Appointment;
import exception.PatientNumberNotFoundException;

import java.util.List;
import java.util.Scanner;
import java.sql.Date;

public class MainModule {
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		IHospitalService service = new HospitalServiceImpl();
		
		while (true) {
			System.out.println("\n===== Hospital Management Menu =====");
            System.out.println("1. Get Appointment by ID");
            System.out.println("2. Get Appointments for Patient");
            System.out.println("3. Get Appointments for Doctor");
            System.out.println("4. Schedule Appointment");
            System.out.println("5. Update Appointment");
            System.out.println("6. Cancel Appointment");
            System.out.println("7. Exit");
            System.out.print("Enter choice: ");
            int choice = sc.nextInt();
            sc.nextLine();
            
            try {
            	switch (choice) {
            	case 1:
                    System.out.print("Enter Appointment ID: ");
                    int aid = sc.nextInt();
                    Appointment a = service.getAppointmentById(aid);
                    System.out.println(a);
                    break;
                    
            	case 2:
                    System.out.print("Enter Patient ID: ");
                    int pid = sc.nextInt();
                    List<Appointment> appointmentsForPatient = service.getAppointmentsForPatient(pid);
                    appointmentsForPatient.forEach(System.out::println);
                    break;
                    
            	case 3:
                    System.out.print("Enter Doctor ID: ");
                    int did = sc.nextInt();
                    List<Appointment> appointmentsForDoctor = service.getAppointmentsForDoctor(did);
                    appointmentsForDoctor.forEach(System.out::println);
                    break;
                    
            	case 4:
                    System.out.print("Enter Appointment ID: ");
                    int sid = sc.nextInt();
                    System.out.print("Enter Patient ID: ");
                    int spid = sc.nextInt();
                    System.out.print("Enter Doctor ID: ");
                    int sdocid = sc.nextInt();
                    sc.nextLine();
                    System.out.print("Enter Appointment Date (YYYY-MM-DD): ");
                    String sdateStr = sc.nextLine();
                    Date sdate = Date.valueOf(sdateStr);
                    System.out.print("Enter Description: ");
                    String desc = sc.nextLine();
                    Appointment newApp = new Appointment(sid, spid, sdocid, sdate, desc);
                    if (service.scheduleAppointment(newApp)) {
                        System.out.println("Appointment scheduled successfully.");
                    }
                    break;
                    
            	case 5:
                    System.out.print("Enter Appointment ID to update: ");
                    int uaid = sc.nextInt();
                    System.out.print("Enter new Patient ID: ");
                    int upid = sc.nextInt();
                    System.out.print("Enter new Doctor ID: ");
                    int udid = sc.nextInt();
                    sc.nextLine();
                    System.out.print("Enter new Appointment Date (YYYY-MM-DD): ");
                    String udateStr = sc.nextLine();
                    Date udate = Date.valueOf(udateStr);
                    System.out.print("Enter new Description: ");
                    String udesc = sc.nextLine();
                    Appointment updateApp = new Appointment(uaid, upid, udid, udate, udesc);
                    if (service.updateAppointment(updateApp)) {
                        System.out.println("Appointment updated successfully.");
                    }
                    break;
                    
            	case 6:
                    System.out.print("Enter Appointment ID to cancel: ");
                    int caid = sc.nextInt();
                    if (service.cancelAppointment(caid)) {
                        System.out.println("Appointment cancelled successfully.");
                    }
                    break;
                case 7:
                    System.out.println("Exiting application.");
                    sc.close();
                    System.exit(0);
                    break;
                    
                default:
                    System.out.println("Invalid choice. Try again.");
            	}
            } catch (PatientNumberNotFoundException pnfe) {
                System.out.println("Error: " + pnfe.getMessage());
            } catch (Exception e) {
                System.out.println("Unexpected error: " + e.getMessage());
            }
		}
	}
}