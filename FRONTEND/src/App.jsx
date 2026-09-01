import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import FooterComponent from './compnent/templates/FooterComponent';
import HeaderComponent from './compnent/templates/HeaderComponent';
import ListShipmentComponent from './compnent/shipment/ListShipmentComponent';
import AddShipmentComponent from './compnent/shipment/AddShipmentComponent';
import EditShipmentComponent from './compnent/shipment/EditShipmentComponent';

function App() {
  return (
    <Router>
      <div className="d-flex flex-column min-vh-100 bg-light">
        <HeaderComponent />
        <main className="flex-grow-1">
          <Routes>
            <Route path="/" element={<Navigate to="/list-shipment" replace />} />
            <Route path="/list-shipment" element={<ListShipmentComponent />} />
            <Route path="/add-shipment" element={<AddShipmentComponent />} />
            <Route path="/edit-shipment/:id" element={<EditShipmentComponent />} />
          </Routes>
        </main>
        <FooterComponent />
      </div>
    </Router>
  );
}

export default App;