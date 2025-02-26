// src/components/Login.js
import React, { useState } from 'react';
import { Container, Typography, TextField, Button, Box } from '@mui/material';
import { useNavigate } from 'react-router-dom';
import axiosInstance from '../api/axiosConfig';
import { setToken } from '../utils/tokenUtils';  // tokenUtils import 추가

const Login = () => {
    const navigate = useNavigate();
    const [formData, setFormData] = useState({
        email: '',
        password: ''
    });

    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData(prevState => ({
            ...prevState,
            [name]: value
        }));
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            const response = await axiosInstance.post('/api/users/login', {
                email: formData.email,
                password: formData.password
            });
            
            if (setToken(response.data.data)) {
                navigate('/ledgers');
            } else {
                throw new Error('토큰 저장 실패');
            }
        } catch (error) {
            console.error('로그인 실패:', error);
            alert('로그인에 실패했습니다.');
        }
    };

    return (
        <Container maxWidth="xs">
            <Box sx={{ mt: 8, display: 'flex', flexDirection: 'column', alignItems: 'center' }}>
                <Typography component="h1" variant="h5">
                    로그인
                </Typography>
                <Box component="form" onSubmit={handleSubmit} sx={{ mt: 1 }}>
                    <TextField
                        margin="normal"
                        required
                        fullWidth
                        label="이메일"
                        name="email"
                        value={formData.email}
                        onChange={handleChange}
                        autoFocus
                    />
                    <TextField
                        margin="normal"
                        required
                        fullWidth
                        label="비밀번호"
                        name="password"
                        type="password"
                        value={formData.password}
                        onChange={handleChange}
                    />
                    <Button
                        type="submit"
                        fullWidth
                        variant="contained"
                        sx={{ mt: 3, mb: 2 }}
                    >
                        로그인
                    </Button>
                </Box>
            </Box>
        </Container>
    );
};

export default Login;