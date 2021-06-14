package com.likesby.bludoc.ModelLayer.Entities;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.SerializedName;
import com.likesby.bludoc.ModelLayer.NewEntities.LabTestItem;

public class PrescriptionJSON{

	@SerializedName("doctor_id")
	private String doctorId;

	@SerializedName("medicines")
	private ArrayList<MedicinesItem> medicines;

	public ArrayList<LabTestItem> getLabTest() {
		return labTest;
	}

	public void setLabTest(ArrayList<LabTestItem> labTest) {
		this.labTest = labTest;
	}

	public String getEndNote() {
		return endNote;
	}

	public void setEndNote(String endNote) {
		this.endNote = endNote;
	}

	@SerializedName("lab_test")
	private ArrayList<LabTestItem> labTest;

	@SerializedName("end_note")
	private String endNote;

	@SerializedName("is_certificate")
	private String isCertificate;

	@SerializedName("temperature")
	private String temperature;

	@SerializedName("height")
	private String height;

	@SerializedName("weight")
	private String weight;

	@SerializedName("date")
	private String date;

	@SerializedName("pulse")
	private String pulse;

	@SerializedName("blood_pressure")
	private String blood_pressure;

	@SerializedName("blood_sugar")
	private String blood_sugar;

	@SerializedName("hemoglobin")
	private String hemoglobin;

	@SerializedName("spo2")
	private String spo2;

	@SerializedName("respiration_rate")
	private String respiration_rate;

	@SerializedName("patient_id")
	private String patientId;

	@SerializedName("allergy")
	private String allergy;

	public String getDiagnosis() {
		return diagnosis;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public void setDiagnosis(String diagnosis) {
		this.diagnosis = diagnosis;
	}

	@SerializedName("diagnosis")
	private String diagnosis;

	public void setDoctorId(String doctorId){
		this.doctorId = doctorId;
	}

	public String getDoctorId(){
		return doctorId;
	}

	public String getIsCertificate() {
		return isCertificate;
	}

	public void setIsCertificate(String isCertificate) {
		this.isCertificate = isCertificate;
	}

	public void setMedicines(ArrayList<MedicinesItem> medicines){
		this.medicines = medicines;
	}

	public ArrayList<MedicinesItem> getMedicines(){
		return medicines;
	}

	public void setPatientId(String patientId){
		this.patientId = patientId;
	}

	public String getPatientId(){
		return patientId;
	}

	public String getTemperature() {
		return temperature;
	}

	public void setTemperature(String temperature) {
		this.temperature = temperature;
	}

	public String getHeight() {
		return height;
	}

	public void setHeight(String height) {
		this.height = height;
	}

	public String getWeight() {
		return weight;
	}

	public void setWeight(String weight) {
		this.weight = weight;
	}

	public String getPulse() {
		return pulse;
	}

	public void setPulse(String pulse) {
		this.pulse = pulse;
	}

	public String getBlood_pressure() {
		return blood_pressure;
	}

	public void setBlood_pressure(String blood_pressure) {
		this.blood_pressure = blood_pressure;
	}

	public String getBlood_sugar() {
		return blood_sugar;
	}

	public void setBlood_sugar(String blood_sugar) {
		this.blood_sugar = blood_sugar;
	}

	public String getHemoglobin() {
		return hemoglobin;
	}

	public void setHemoglobin(String hemoglobin) {
		this.hemoglobin = hemoglobin;
	}

	public String getSpo2() {
		return spo2;
	}

	public void setSpo2(String spo2) {
		this.spo2 = spo2;
	}

	public String getRespiration_rate() {
		return respiration_rate;
	}

	public void setRespiration_rate(String respiration_rate) {
		this.respiration_rate = respiration_rate;
	}

	public String getAllergy() {
		return allergy;
	}

	public void setAllergy(String allergy) {
		this.allergy = allergy;
	}

	@Override
 	public String toString(){
		return 
			"PrescriptionJSON{" + 
			"doctor_id = '" + doctorId + '\'' + 
			",medicines = '" + medicines + '\'' + 
			",patient_id = '" + patientId + '\'' + 
			",end_note = '" + endNote + '\'' +
			",lab_test = '" + labTest + '\'' +
			",temperature = '" + temperature + '\'' +
			",height = '" + height + '\'' +
			",weight = '" + weight + '\'' +
			",pulse = '" + pulse + '\'' +
			",blood_pressure = '" + blood_pressure + '\'' +
			",blood_sugar = '" + blood_sugar + '\'' +
			",hemoglobin = '" + hemoglobin + '\'' +
			",spo2 = '" + spo2 + '\'' +
			",respiration_rate = '" + respiration_rate + '\'' +
			",allergy = '" + allergy + '\'' +
			"}";
		}
}