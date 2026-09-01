import React, { useEffect, useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import { getShipmentsByStatus, deleteShipment, refreshWeather } from '../../services/ShipmentService';

const ListShipmentComponent = () => {
  const [shipments, setShipments] = useState([]);
  const [selectedStatus, setSelectedStatus] = useState('');
  const [selectedRisk, setSelectedRisk] = useState('');
  const [startDate, setStartDate] = useState('');
  const [endDate, setEndDate] = useState('');
  const [loading, setLoading] = useState(true);
  const [message, setMessage] = useState('');
  const [refreshingId, setRefreshingId] = useState(null);

  const [showDeleteModal, setShowDeleteModal] = useState(false);
  const [deletingId, setDeletingId] = useState(null);

  const navigate = useNavigate();

  useEffect(() => {
    fetchShipments();
  }, [selectedStatus]);

  const fetchShipments = async () => {
    setLoading(true);
    try {
      const response = await getShipmentsByStatus(selectedStatus);
      if (response.data && response.data.data) {
        setShipments(response.data.data);
      } else {
        setShipments([]);
      }
    } catch (error) {
      setMessage("Failed to load shipments.");
    } finally {
      setLoading(false);
    }
  };

  const openDeleteModal = (id) => {
    setDeletingId(id);
    setShowDeleteModal(true);
  };

  const handleExecuteDelete = async () => {
    if (!deletingId) return;
    try {
      await deleteShipment(deletingId);
      setMessage("Shipment deleted successfully.");
      fetchShipments();
    } catch (error) {
      setMessage("Failed to delete shipment.");
    } finally {
      setShowDeleteModal(false);
      setDeletingId(null);
    }
  };

  const handleRefreshWeather = async (id) => {
    setRefreshingId(id);
    try {
      const res = await refreshWeather(id);
      if (res.data && res.data.status === 200) {
        setMessage("Weather risk updated successfully.");
        fetchShipments();
      } else {
        setMessage(res.data.message || "Failed to update weather data.");
      }
    } catch (error) {
      setMessage("Failed to update weather data.");
    } finally {
      setRefreshingId(null);
    }
  };

  const formatFetchedAt = (dateStr) => {
    if (!dateStr) return '-';
    const cleanStr = String(dateStr).replace(' ', 'T');
    const d = new Date(cleanStr);
    if (isNaN(d.getTime())) return dateStr;
    const hours = String(d.getHours()).padStart(2, '0');
    const minutes = String(d.getMinutes()).padStart(2, '0');
    const seconds = String(d.getSeconds()).padStart(2, '0');
    const day = String(d.getDate()).padStart(2, '0');
    const months = ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'];
    const month = months[d.getMonth()];
    const year = String(d.getFullYear()).slice(-2);
    return `${hours}:${minutes}:${seconds} ${day} ${month} ${year}`;
  };

  const formatDispatchDate = (dateStr) => {
    if (!dateStr) return '-';
    const cleanStr = String(dateStr).replace(' ', 'T');
    const d = new Date(cleanStr);
    if (isNaN(d.getTime())) return dateStr;
    const day = String(d.getDate()).padStart(2, '0');
    const months = ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'];
    const month = months[d.getMonth()];
    const year = String(d.getFullYear()).slice(-2);
    return `${day} ${month} ${year}`;
  };

  const getRiskBadgeClass = (risk) => {
    switch (risk) {
      case 'LOW':
        return 'badge bg-success';
      case 'MEDIUM':
        return 'badge bg-warning text-dark';
      case 'HIGH':
        return 'badge bg-danger';
      default:
        return 'badge bg-secondary';
    }
  };

  const getStatusBadgeClass = (status) => {
    switch (status) {
      case 'PLANNED':
        return 'badge bg-primary';
      case 'DISPATCHED':
        return 'badge bg-info text-dark';
      case 'DELIVERED':
        return 'badge bg-success';
      case 'CANCELLED':
        return 'badge bg-secondary';
      default:
        return 'badge bg-dark';
    }
  };

  const filteredShipments = shipments.filter((s) => {
    if (selectedRisk && (s.risk_level || 'UNKNOWN') !== selectedRisk) {
      return false;
    }
    if (startDate && endDate) {
      if (s.dispatch_date < startDate || s.dispatch_date > endDate) {
        return false;
      }
    }
    return true;
  });

  return (
    <div className="container-fluid px-4 mt-4 mb-5">
      <h2 className="text-center mb-4">Outbound Shipment Board</h2>

      {message && (
        <div className="alert alert-info alert-dismissible fade show" role="alert">
          {message}
          <button type="button" className="btn-close" onClick={() => setMessage('')}></button>
        </div>
      )}

      {((startDate && !endDate) || (!startDate && endDate)) && (
        <div className="alert alert-warning py-2 mb-3" role="alert">
          Both Start Date and End Date must be selected to filter by date range.
        </div>
      )}

      <div className="d-flex justify-content-between align-items-center flex-wrap gap-3 mb-3 card p-3 bg-light border-0 shadow-sm">
        <div>
          <Link to="/add-shipment" className="btn btn-primary">
            Add Shipment
          </Link>
        </div>
        
        <div className="d-flex align-items-center flex-wrap gap-3">
          <div className="d-flex align-items-center gap-2">
            <label className="mb-0 fw-semibold">Status:</label>
            <select 
              className="form-select w-auto form-select-sm" 
              value={selectedStatus} 
              onChange={(e) => setSelectedStatus(e.target.value)}
            >
              <option value="">All Statuses</option>
              <option value="PLANNED">PLANNED</option>
              <option value="DISPATCHED">DISPATCHED</option>
              <option value="DELIVERED">DELIVERED</option>
              <option value="CANCELLED">CANCELLED</option>
            </select>
          </div>

          <div className="d-flex align-items-center gap-2">
            <label className="mb-0 fw-semibold">Risk Level:</label>
            <select 
              className="form-select w-auto form-select-sm" 
              value={selectedRisk} 
              onChange={(e) => setSelectedRisk(e.target.value)}
            >
              <option value="">All Risks</option>
              <option value="LOW">LOW</option>
              <option value="MEDIUM">MEDIUM</option>
              <option value="HIGH">HIGH</option>
              <option value="UNKNOWN">UNKNOWN</option>
            </select>
          </div>

          <div className="d-flex align-items-center gap-2">
            <label className="mb-0 fw-semibold">From Date:</label>
            <input 
              type="date" 
              className="form-control form-control-sm w-auto"
              value={startDate}
              onChange={(e) => setStartDate(e.target.value)}
            />
            <label className="mb-0 fw-semibold">To Date:</label>
            <input 
              type="date" 
              className="form-control form-control-sm w-auto"
              value={endDate}
              onChange={(e) => setEndDate(e.target.value)}
            />
          </div>

          {(startDate || endDate || selectedStatus || selectedRisk) && (
            <button 
              className="btn btn-outline-secondary btn-sm"
              onClick={() => {
                setStartDate('');
                setEndDate('');
                setSelectedStatus('');
                setSelectedRisk('');
              }}
            >
              Reset Filter (All Time)
            </button>
          )}
        </div>
      </div>

      <div className="table-responsive">
        <table className="table table-striped table-bordered align-middle w-100 mb-0">
          <thead className="table-light text-nowrap">
            <tr className="text-center align-middle">
              <th className="text-center align-middle">Product Code</th>
              <th className="text-center align-middle">Quantity</th>
              <th className="text-center align-middle">Destination City</th>
              <th className="text-center align-middle">Coordinates</th>
              <th className="text-center align-middle">Dispatch Date</th>
              <th className="text-center align-middle">Status</th>
              <th className="text-center align-middle">Risk Level</th>
              <th className="text-center align-middle">Precip (mm)</th>
              <th className="text-center align-middle">Fetched At</th>
              <th className="text-center align-middle">Actions</th>
            </tr>
          </thead>
          <tbody>
            {loading ? (
              <tr>
                <td colSpan="10" className="text-center py-4">Loading shipments...</td>
              </tr>
            ) : filteredShipments.length === 0 ? (
              <tr>
                <td colSpan="10" className="text-center py-4">No shipments found.</td>
              </tr>
            ) : (
              filteredShipments.map((s) => (
                <tr key={s.id}>
                  <td className="text-nowrap">{s.product_code}</td>
                  <td className="text-end text-nowrap">{Number(s.quantity).toLocaleString()} pcs</td>
                  <td className="text-nowrap">{s.dest_city}</td>
                  <td className="text-center text-nowrap">
                    <a 
                      href={`https://www.google.com/maps?q=${s.dest_lat},${s.dest_lng}`} 
                      target="_blank" 
                      rel="noopener noreferrer"
                      className="text-primary text-decoration-underline"
                    >
                      Lat: {s.dest_lat}, Lng: {s.dest_lng}
                    </a>
                  </td>
                  <td className="text-center text-nowrap">{formatDispatchDate(s.dispatch_date)}</td>
                  <td className="text-center text-nowrap">
                    <span className={getStatusBadgeClass(s.status)}>{s.status}</span>
                  </td>
                  <td className="text-center text-nowrap">
                    <span className={getRiskBadgeClass(s.risk_level)}>
                      {s.risk_level || 'UNKNOWN'}
                    </span>
                  </td>
                  <td className="text-end text-nowrap">
                    {s.precip_mm !== null && s.precip_mm !== undefined ? `${s.precip_mm} mm` : '-'}
                  </td>
                  <td className="text-center text-nowrap">{formatFetchedAt(s.fetched_at)}</td>
                  <td className="text-center text-nowrap">
                    <button 
                      className="btn btn-info btn-sm me-1 text-white" 
                      disabled={refreshingId === s.id}
                      onClick={() => handleRefreshWeather(s.id)}
                    >
                      {refreshingId === s.id ? 'Refreshing...' : 'Refresh Weather'}
                    </button>
                    <button 
                      className="btn btn-warning btn-sm me-1" 
                      onClick={() => navigate(`/edit-shipment/${s.id}`)}
                    >
                      Edit
                    </button>
                    <button 
                      className="btn btn-danger btn-sm" 
                      onClick={() => openDeleteModal(s.id)}
                    >
                      Delete
                    </button>
                  </td>
                </tr>
              ))
            )}
          </tbody>
        </table>
      </div>

      {showDeleteModal && (
        <div className="modal fade show d-block" tabIndex="-1" style={{ backgroundColor: 'rgba(0,0,0,0.5)' }}>
          <div className="modal-dialog modal-dialog-centered">
            <div className="modal-content">
              <div className="modal-header">
                <h5 className="modal-title fw-bold">Confirm Delete</h5>
                <button type="button" className="btn-close" onClick={() => setShowDeleteModal(false)}></button>
              </div>
              <div className="modal-body">
                <p className="mb-0">Are you sure you want to delete this shipment?</p>
              </div>
              <div className="modal-footer">
                <button type="button" className="btn btn-secondary" onClick={() => setShowDeleteModal(false)}>
                  Cancel
                </button>
                <button type="button" className="btn btn-danger" onClick={handleExecuteDelete}>
                  Delete
                </button>
              </div>
            </div>
          </div>
        </div>
      )}
    </div>
  );
};

export default ListShipmentComponent;
