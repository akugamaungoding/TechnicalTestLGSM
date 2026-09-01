import React from 'react';
import 'bootstrap/dist/css/bootstrap.min.css';
import { Link } from 'react-router-dom';

function HeaderComponent() {
    return (
        <nav className="navbar navbar-expand-lg navbar-light bg-light">
            <div className="container">
                <Link className="navbar-brand" to="/list-shipment">
                    Outbound Logistics System
                </Link>
            </div>
        </nav>
    );
}

export default HeaderComponent;
