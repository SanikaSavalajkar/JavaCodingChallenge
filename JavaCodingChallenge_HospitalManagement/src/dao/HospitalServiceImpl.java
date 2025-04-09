package dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import entity.Appointment;
import exception.PatientNumberNotFoundException;
import util.DBConnection;

public class HospitalServiceImpl implements IHospitalService {
	
	private static final String PROPERTY_FILE = "db.properties";
	
	@Override
	public Appointment getAppointmentById(int appointmentId) throws PatientNumberNotFoundException {
		Appointment appointment = null;
		try (Connection conn = DBConnection.getConnection(PROPERTY_FILE);
	             PreparedStatement ps = conn.prepareStatement("SELECT * FROM Appointment WHERE appointmentId = ?")) {

	            ps.setInt(1, appointmentId);
	            ResultSet rs = ps.executeQuery();
	            if (rs.next()) {
	                appointment = new Appointment(
	                        rs.getInt("appointmentId"),
	                        rs.getInt("patientId"),
	                        rs.getInt("doctorId"),
	                        rs.getDate("appointmentDate"),
	                        rs.getString("description")
	                );
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
		return appointment;
	}

	@Override
	public List<Appointment> getAppointmentsForPatient(int patientId) throws PatientNumberNotFoundException {
		List<Appointment> list = new ArrayList<>();
		try (Connection conn = DBConnection.getConnection(PROPERTY_FILE);
	             PreparedStatement ps = conn.prepareStatement("SELECT * FROM Appointment WHERE patientId = ?")) {

	            ps.setInt(1, patientId);
	            ResultSet rs = ps.executeQuery();
	            while (rs.next()) {
	                list.add(new Appointment(
	                        rs.getInt("appointmentId"),
	                        rs.getInt("patientId"),
	                        rs.getInt("doctorId"),
	                        rs.getDate("appointmentDate"),
	                        rs.getString("description")
	                ));
	            }
	            if (list.isEmpty()) {
	                throw new PatientNumberNotFoundException("No appointments found for patient ID: " + patientId);
	            }

	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
		return list;
	}

	@Override
	public List<Appointment> getAppointmentsForDoctor(int doctorId) {
		List<Appointment> list = new ArrayList<>();
		try (Connection conn = DBConnection.getConnection(PROPERTY_FILE);
	             PreparedStatement ps = conn.prepareStatement("SELECT * FROM Appointment WHERE doctorId = ?")) {

	            ps.setInt(1, doctorId);
	            ResultSet rs = ps.executeQuery();
	            while (rs.next()) {
	                list.add(new Appointment(
	                        rs.getInt("appointmentId"),
	                        rs.getInt("patientId"),
	                        rs.getInt("doctorId"),
	                        rs.getDate("appointmentDate"),
	                        rs.getString("description")
	                ));
	            }

	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
		return list;
	}

	@Override
	public boolean scheduleAppointment(Appointment appointment) {
		try (Connection conn = DBConnection.getConnection(PROPERTY_FILE);
	             PreparedStatement ps = conn.prepareStatement(
	                     "INSERT INTO Appointment (appointmentId, patientId, doctorId, appointmentDate, description) VALUES (?, ?, ?, ?, ?)")) {

	            ps.setInt(1, appointment.getAppointmentId());
	            ps.setInt(2, appointment.getPatientId());
	            ps.setInt(3, appointment.getDoctorId());
	            ps.setDate(4, (java.sql.Date) appointment.getAppointmentDate());
	            ps.setString(5, appointment.getDescription());

	            int rowsInserted = ps.executeUpdate();
	            return rowsInserted > 0;

	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
		return false;
	}

	@Override
	public boolean updateAppointment(Appointment appointment) {
		try (Connection conn = DBConnection.getConnection(PROPERTY_FILE);
	             PreparedStatement ps = conn.prepareStatement(
	                     "UPDATE Appointment SET patientId = ?, doctorId = ?, appointmentDate = ?, description = ? WHERE appointmentId = ?")) {

	            ps.setInt(1, appointment.getPatientId());
	            ps.setInt(2, appointment.getDoctorId());
	            ps.setDate(3, (Date) appointment.getAppointmentDate());
	            ps.setString(4, appointment.getDescription());
	            ps.setInt(5, appointment.getAppointmentId());

	            int rowsUpdated = ps.executeUpdate();
	            return rowsUpdated > 0;

	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
		return false;
	}

	@Override
	public boolean cancelAppointment(int appointmentId) {
		try (Connection conn = DBConnection.getConnection(PROPERTY_FILE);
	             PreparedStatement ps = conn.prepareStatement("DELETE FROM Appointment WHERE appointmentId = ?")) {

	            ps.setInt(1, appointmentId);
	            int rowsDeleted = ps.executeUpdate();
	            return rowsDeleted > 0;

	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
		return false;
	}
}