package dao;

import entity.Appointment;
import exception.PatientNumberNotFoundException;
import util.DBConnUtil;

import java.sql.*;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class HospitalServiceImpl implements IHospitalService {
    private static final String PROPERTY_FILE = "db.properties";

    @Override
    public Appointment getAppointmentById(int id) {
        Appointment appt = null;
        try (Connection con = DBConnUtil.getDbConnection()) {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM appointment WHERE appointmentid=?");
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                appt = new Appointment(rs.getInt(1), rs.getInt(2), rs.getInt(3),
                        rs.getDate(4), rs.getString(5));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return appt;
    }

    @Override
    public List<Appointment> getAppointmentsForPatient(int patientId) throws PatientNumberNotFoundException {
        List<Appointment> list = new ArrayList<>();
        try (Connection con = DBConnUtil.getDbConnection()) {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM appointment WHERE patientid=?");
            ps.setInt(1, patientId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new Appointment(rs.getInt(1), rs.getInt(2), rs.getInt(3),
                        rs.getDate(4), rs.getString(5)));
            }
            if (list.isEmpty()) {
                throw new PatientNumberNotFoundException("No appointments for patient ID: " + patientId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public List<Appointment> getAppointmentsForDoctor(int doctorId) {
        List<Appointment> list = new ArrayList<>();
        try (Connection con = DBConnUtil.getDbConnection()) {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM appointment WHERE doctorid=?");
            ps.setInt(1, doctorId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new Appointment(rs.getInt(1), rs.getInt(2), rs.getInt(3),
                        rs.getDate(4), rs.getString(5)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public boolean scheduleAppointment(Appointment appointment) {
        try (Connection con = DBConnUtil.getDbConnection()) {
            PreparedStatement ps = con.prepareStatement("INSERT INTO appointment VALUES (?, ?, ?, ?, ?)");
            ps.setInt(1, appointment.getAppointmentId());
            ps.setInt(2, appointment.getPatientId());
            ps.setInt(3, appointment.getDoctorId());
            ps.setDate(4, (Date) appointment.getAppointmentDate());
            ps.setString(5, appointment.getDescription());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean updateAppointment(Appointment appointment) {
        try (Connection con = DBConnUtil.getDbConnection()) {
            PreparedStatement ps = con.prepareStatement(
                    "UPDATE appointment SET patientid=?, doctorid=?, appointmentdate=?, description=? WHERE appointmentid=?");
            ps.setInt(1, appointment.getPatientId());
            ps.setInt(2, appointment.getDoctorId());
            ps.setDate(3, (Date) appointment.getAppointmentDate());
            ps.setString(4, appointment.getDescription());
            ps.setInt(5, appointment.getAppointmentId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean cancelAppointment(int appointmentId) {
        try (Connection con = DBConnUtil.getDbConnection()) {
            PreparedStatement ps = con.prepareStatement("DELETE FROM appointment WHERE appointmentid=?");
            ps.setInt(1, appointmentId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
