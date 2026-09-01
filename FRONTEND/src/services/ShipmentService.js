import axios from 'axios';

const SHIPMENT_API_BASE_URL = "http://localhost:8080/shipment";

export const getAllShipments = () => {
    return axios.get(`${SHIPMENT_API_BASE_URL}/getAllShipments`);
};

export const getShipmentsByStatus = (status) => {
    if (status && status.trim() !== "") {
        return axios.get(`${SHIPMENT_API_BASE_URL}/getShipmentsByStatus?status=${encodeURIComponent(status)}`);
    }
    return axios.get(`${SHIPMENT_API_BASE_URL}/getAllShipments`);
};

export const getShipmentById = (shipmentId) => {
    return axios.get(`${SHIPMENT_API_BASE_URL}/getShipmentById/${shipmentId}`);
};

export const checkProductCodeExists = (productCode, id) => {
    let url = `${SHIPMENT_API_BASE_URL}/checkProductCode?product_code=${encodeURIComponent(productCode)}`;
    if (id) {
        url += `&id=${id}`;
    }
    return axios.get(url);
};

export const getCityPresets = () => {
    return axios.get(`${SHIPMENT_API_BASE_URL}/getCityPresets`);
};

export const createShipment = (shipment) => {
    return axios.post(`${SHIPMENT_API_BASE_URL}/createShipment`, shipment);
};

export const editShipment = (shipment) => {
    return axios.post(`${SHIPMENT_API_BASE_URL}/editShipment`, shipment);
};

export const deleteShipment = (shipmentId) => {
    return axios.delete(`${SHIPMENT_API_BASE_URL}/deleteShipment/${shipmentId}`);
};

export const refreshWeather = (shipmentId) => {
    return axios.post(`${SHIPMENT_API_BASE_URL}/refreshWeather/${shipmentId}`);
};
