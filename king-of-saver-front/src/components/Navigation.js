// src/components/Navigation.js
import React from 'react';
import { AppBar, Toolbar, Button, Typography } from '@mui/material';
import { useNavigate } from 'react-router-dom';

const Navigation = () => {
    const navigate = useNavigate();
    const isLoggedIn = localStorage.getItem('token');

    const handleLogout = () => {
        localStorage.removeItem('token');
        navigate('/login');
    };

    return (
        <AppBar position="static">
            <Toolbar>
                <Typography variant="h6" sx={{ flexGrow: 1 }}>
                    My Budget Community
                </Typography>
                {isLoggedIn ? (
                    <>
                        <Button color="inherit" onClick={() => navigate('/posts')}>게시글</Button>
                        <Button color="inherit" onClick={() => navigate('/ledgers')}>가계부</Button>
                        <Button color="inherit" onClick={() => navigate('/todos')}>할 일</Button>
                        <Button color="inherit" onClick={handleLogout}>로그아웃</Button>
                    </>
                ) : (
                    <>
                        <Button color="inherit" onClick={() => navigate('/login')}>로그인</Button>
                        <Button color="inherit" onClick={() => navigate('/register')}>회원가입</Button>
                    </>
                )}
            </Toolbar>
        </AppBar>
    );
};

export default Navigation;