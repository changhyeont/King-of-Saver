import React from 'react';
import { BrowserRouter as Router, Route, Routes, Navigate } from 'react-router-dom';
import Login from './components/Login';
import Register from './components/Register';
import Posts from './components/Posts';
import Ledgers from './components/Ledgers';
import Todos from './components/Todos';
import Navigation from './components/Navigation';
import { LocalizationProvider } from '@mui/x-date-pickers';
import { AdapterDayjs } from '@mui/x-date-pickers/AdapterDayjs';
import dayjs from 'dayjs';
import 'dayjs/locale/ko';

dayjs.locale('ko');

function App() {
  return (
    <LocalizationProvider dateAdapter={AdapterDayjs} adapterLocale="ko">
      <Router>
        <Navigation />
        <div className="container">
          <Routes>
            <Route path="/" element={<Navigate to="/posts" />} />
            <Route path="/login" element={<Login />} />
            <Route path="/register" element={<Register />} />
            <Route path="/posts" element={<Posts />} />
            <Route path="/ledgers" element={<Ledgers />} />
            <Route path="/todos" element={<Todos />} />
          </Routes>
        </div>
      </Router>
    </LocalizationProvider>
  );
}

export default App;
