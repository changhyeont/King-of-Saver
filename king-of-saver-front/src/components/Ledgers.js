import React, { useState, useEffect, useCallback } from 'react';
import { useNavigate } from 'react-router-dom';
import {
    Container, Grid, Paper, Typography, TextField, Select,
    MenuItem, Button, Box, IconButton
} from '@mui/material';
import { DateCalendar } from '@mui/x-date-pickers/DateCalendar';
import { DatePicker } from '@mui/x-date-pickers/DatePicker';
import axiosInstance from '../api/axiosConfig';
import dayjs from 'dayjs';
import 'dayjs/locale/ko'; // 한국어 로케일 추가

// dayjs 한국어 설정
dayjs.locale('ko');

const Ledgers = () => {
    const navigate = useNavigate();
    const [isLoading, setIsLoading] = useState(true);
    const [selectedDate, setSelectedDate] = useState(dayjs());
    const [editLedger, setEditLedger] = useState(null);

    // monthlyStats 초기값 설정
    const [monthlyStats, setMonthlyStats] = useState({
        totalIncome: 0,
        totalExpense: 0,
        balance: 0,
        dailyStats: {} // 빈 객체로 초기화
    });

    // 날짜별 데이터 조회
    const getDailyStats = useCallback((date) => {
        if (!date) return { income: 0, expense: 0, items: [] };

        const formattedDate = date.format('YYYY-MM-DD');
        return monthlyStats.dailyStats[formattedDate] || {
            income: 0,
            expense: 0,
            items: []
        };
    }, [monthlyStats.dailyStats]);

    const [dailyLedgers, setDailyLedgers] = useState([]);

    // 일별 내역 조회 함수 개선
    const fetchDailyLedgers = useCallback(async (date) => {
        try {
            console.log('일별 내역 조회 요청:', date.format('YYYY-MM-DD'));
            const response = await axiosInstance.get(
                `/api/ledgers/daily/${date.format('YYYY-MM-DD')}`
            );
            
            console.log('일별 내역 응답:', response.data);
            
            if (response.data?.data) {
                setDailyLedgers(response.data.data);
                console.log('설정된 일별 내역:', response.data.data);
            }
        } catch (error) {
            console.error('일별 내역 조회 실패:', error);
            setDailyLedgers([]);
        }
    }, []);

    // 날짜 선택 시 데이터 조회
    useEffect(() => {
        if (!isLoading) {
            fetchDailyLedgers(selectedDate);
        }
    }, [selectedDate, fetchDailyLedgers, isLoading]);

    // 선택된 날짜의 내역 표시 부분 수정
    const renderDailyTransactions = () => {
        // 일별 합계 계산 수정
        const dailyTotal = dailyLedgers.reduce((acc, ledger) => ({
            income: acc.income + (ledger.income || 0),
            expense: acc.expense + (ledger.expense || 0)
        }), { income: 0, expense: 0 });

        return (
            <Box>
                {/* 일별 합계 */}
                <Paper sx={{ mb: 3, p: 2 }}>
                    <Typography variant="h6" gutterBottom>
                        {selectedDate.format('YYYY년 MM월 DD일')} 합계
                    </Typography>
                    <Box sx={{ display: 'flex', justifyContent: 'space-between' }}>
                        <Typography color="success.main">
                            수입: {dailyTotal.income.toLocaleString()}원
                        </Typography>
                        <Typography color="error.main">
                            지출: {dailyTotal.expense.toLocaleString()}원
                        </Typography>
                    </Box>
                </Paper>

                {/* 상세 내역 */}
                {dailyLedgers.map((ledger, index) => (
                    <Paper key={ledger.id || index} sx={{ mb: 2, p: 2 }}>
                        <Grid container alignItems="center" spacing={2}>
                            <Grid item xs={3}>
                                <Typography color={ledger.income > 0 ? "success.main" : "error.main"}>
                                    {ledger.income > 0 ? '수입' : '지출'}
                                </Typography>
                            </Grid>
                            <Grid item xs={3}>
                                <Typography>
                                    {ledger.income > 0 
                                        ? incomeCategories.find(cat => cat.value === ledger.incomeCategory)?.label
                                        : expenseCategories.find(cat => cat.value === ledger.expenseCategory)?.label
                                    }
                                </Typography>
                            </Grid>
                            <Grid item xs={3}>
                                <Typography>{ledger.description}</Typography>
                            </Grid>
                            <Grid item xs={3}>
                                <Typography 
                                    align="right" 
                                    color={ledger.income > 0 ? "success.main" : "error.main"}
                                >
                                    {(ledger.income || ledger.expense).toLocaleString()}원
                                </Typography>
                            </Grid>
                            <Grid item xs={3}>
                                <Button onClick={() => handleEditClick(ledger)}>수정</Button>
                                <Button onClick={() => handleDelete(ledger.date)}>삭제</Button>
                            </Grid>
                        </Grid>
                    </Paper>
                ))}
            </Box>
        );
    };

    const [formData, setFormData] = useState({
        amount: '',
        description: '',
        category: 'FOOD',
        date: dayjs().format('YYYY-MM-DD'),
        isIncome: false
    });

    // 수입 카테고리 목록
    const incomeCategories = [
        { value: 'SALARY', label: '급여' },
        { value: 'BONUS', label: '상여금' },
        { value: 'INTEREST', label: '이자수입' },
        { value: 'BUSINESS', label: '사업수입' },
        { value: 'RENTAL', label: '임대수입' },
        { value: 'OTHER', label: '기타' }
    ];

    // 지출 카테고리 목록
    const expenseCategories = [
        { value: 'TRANSPORT', label: '교통비' },
        { value: 'UTILITIES', label: '공과금' },
        { value: 'PERSONAL', label: '품위유지' },
        { value: 'HOBBY', label: '취미' },
        { value: 'FOOD', label: '식비' },
        { value: 'DINING', label: '식비(외식)' },
        { value: 'EDUCATION', label: '학업' },
        { value: 'DATE', label: '데이트' },
        { value: 'OTHER', label: '기타' }
    ];

    // 수입/지출 전환 시 카테고리 초기화
    const handleIncomeToggle = (isIncome) => {
        setFormData({
            ...formData,
            isIncome,
            category: isIncome ? 'SALARY' : 'FOOD'  // 각각 기본값으로 설정
        });
    };

    // handleSubmit 함수 수정
    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            const amount = Number(formData.amount);
            const data = {
                date: formData.date,
                amount: amount,
                isIncome: formData.isIncome,
                incomeCategory: formData.isIncome ? formData.category : null,
                expenseCategory: !formData.isIncome ? formData.category : null,
                description: formData.description,
                // 수입/지출 구분에 따라 income/expense 설정
                income: formData.isIncome ? amount : 0,
                expense: !formData.isIncome ? amount : 0
            };

            console.log('전송 데이터:', data);
            const response = await axiosInstance.post('/api/ledgers', data);
            console.log('저장된 데이터:', response.data);

            // 데이터 새로고침
            fetchMonthlyStats();
            fetchDailyLedgers(selectedDate);
            
            // 폼 초기화
            setFormData({
                amount: '',
                category: formData.isIncome ? 'SALARY' : 'FOOD',
                description: '',
                date: selectedDate.format('YYYY-MM-DD'),
                isIncome: formData.isIncome
            });
        } catch (error) {
            console.error('가계부 저장 실패:', error);
            alert('가계부 저장에 실패했습니다.');
        }
    };

    const handleSave = async () => {
        try {
            const response = await axiosInstance.post('/api/ledgers', formData);
            fetchDailyLedgers(selectedDate);
            setFormData({
                amount: '',
                category: formData.isIncome ? 'SALARY' : 'FOOD',
                description: '',
                date: selectedDate.format('YYYY-MM-DD'),
                isIncome: formData.isIncome
            });
        } catch (error) {
            console.error('가계부 저장 실패:', error);
            alert('가계부 저장에 실패했습니다.');
        }
    };

    const handleUpdate = async () => {
        try {
            await axiosInstance.put(`/api/ledgers/daily/${editLedger.date}`, {
                income: editLedger.isIncome ? editLedger.amount : 0,
                expense: !editLedger.isIncome ? editLedger.amount : 0,
                description: editLedger.description
            });
            setEditLedger(null);
            // 일별 내역과 월별 통계 모두 새로고침
            fetchDailyLedgers(selectedDate);
            fetchMonthlyStats(); // 월별 통계 새로고침 추가
        } catch (error) {
            console.error('가계부 수정 실패:', error);
            alert('가계부 수정에 실패했습니다.');
        }
    };

    const handleDelete = async (date) => {
        try {
            await axiosInstance.delete(`/api/ledgers/daily/${date}`);
            // 일별 내역과 월별 통계 모두 새로고침
            fetchDailyLedgers(selectedDate);
            fetchMonthlyStats(); // 월별 통계 새로고침 추가
        } catch (error) {
            console.error('가계부 삭제 실패:', error);
            alert('가계부 삭제에 실패했습니다.');
        }
    };

    const handleEditClick = (ledger) => {
        setEditLedger({
            ...ledger,
            amount: ledger.isIncome ? ledger.income : ledger.expense
        });
    };

    const handleCancelEdit = () => {
        setEditLedger(null);
    };

    // categories 배열을 실제 사용되는 곳으로 이동
    const currentCategories = formData.isIncome ? incomeCategories : expenseCategories;

    // axios 기본 설정
    useEffect(() => {
        const token = localStorage.getItem('token');
        if (!token) {
            navigate('/login');
            return;
        }

        // axios 기본 설정
        axiosInstance.defaults.baseURL = 'http://localhost:8080';
        axiosInstance.defaults.headers.common['Authorization'] = token;
        axiosInstance.defaults.headers.post['Content-Type'] = 'application/json';

        setIsLoading(false);
    }, [navigate]);

    useEffect(() => {
        const checkAuth = async () => {
            const token = localStorage.getItem('token');
            if (!token) {
                navigate('/login');
                return;
            }

            try {
                // 토큰 유효성 검사를 위한 API 호출
                await axiosInstance.get('/api/users/validate-token');
                setIsLoading(false);
            } catch (error) {
                console.error('토큰 검증 실패:', error);
                localStorage.removeItem('token');
                navigate('/login');
            }
        };

        checkAuth();
    }, [navigate]);

    useEffect(() => {
        // axios interceptor 설정
        const interceptor = axiosInstance.interceptors.request.use(
            (config) => {
                const token = localStorage.getItem('token');
                if (token) {
                    config.headers['Authorization'] = token;
                }
                return config;
            },
            (error) => {
                return Promise.reject(error);
            }
        );

        // 컴포넌트 언마운트 시 interceptor 제거
        return () => {
            axiosInstance.interceptors.request.eject(interceptor);
        };
    }, []);

    const fetchMonthlyStats = useCallback(async () => {
        if (isLoading) return;

        try {
            const response = await axiosInstance.get(
                `/api/ledgers/monthly/${selectedDate.year()}/${selectedDate.month() + 1}`
            );

            if (response.data?.data) {
                setMonthlyStats(prev => ({
                    ...prev,
                    ...response.data.data,
                    dailyStats: response.data.data.dailyStats || {}
                }));
            }
        } catch (error) {
            console.error('API 호출 오류:', error);
        }
    }, [isLoading, selectedDate]); // 의존성 추가

    // 컴포넌트 마운트 시 실행되는 useEffect
    useEffect(() => {
        const token = localStorage.getItem('token'); // getToken() 대신 직접 localStorage 사용
        if (!token) {
            navigate('/login');
            return;
        }
        fetchMonthlyStats();
    }, [fetchMonthlyStats, navigate]); // fetchMonthlyStats 의존성 추가

    // 날짜 변경 시 데이터 업데이트
    useEffect(() => {
        fetchMonthlyStats();
    }, [selectedDate, fetchMonthlyStats]);

    // 달력에서 날짜 렌더링
    const renderDay = (day, _value, DayComponentProps) => {
        const formattedDate = day.format('YYYY-MM-DD');
        const dailyData = getDailyStats(day);
        
        return (
            <Box 
                sx={{
                    display: 'flex',
                    flexDirection: 'column',
                    alignItems: 'center',
                    cursor: 'pointer'  // 커서 스타일 추가
                }}
                onClick={() => handleDateClick(day)}  // 날짜 클릭 시 선택된 날짜 업데이트
            >
                <IconButton {...DayComponentProps}>
                    {day.date()}
                </IconButton>
                {dailyData && (
                    <Box sx={{ fontSize: '0.7rem' }}>
                        {dailyData.income > 0 && (
                            <Typography color="success.main" variant="caption">
                                +{dailyData.income.toLocaleString()}
                            </Typography>
                        )}
                        {dailyData.expense > 0 && (
                            <Typography color="error.main" variant="caption">
                                -{dailyData.expense.toLocaleString()}
                            </Typography>
                        )}
                    </Box>
                )}
            </Box>
        );
    };

    // 달력 날짜 클릭 처리
    const handleDateClick = (newDate) => {
        console.log('선택된 날짜:', newDate.format('YYYY-MM-DD'));
        setSelectedDate(newDate);
        fetchDailyLedgers(newDate);
        setFormData(prev => ({
            ...prev,
            date: newDate.format('YYYY-MM-DD')
        }));
    };

    // 월별 통계 컴포넌트
    const MonthlyStatsSection = () => (
        <Paper sx={{ p: 3, height: '100%', minHeight: 200 }}>
            <Typography variant="h6" gutterBottom>월별 통계</Typography>
            <Box sx={{ mt: 2 }}>
                <Typography color="success.main">
                    수입: {monthlyStats.totalIncome?.toLocaleString()}원
                </Typography>
                <Typography color="error.main">
                    지출: {monthlyStats.totalExpense?.toLocaleString()}원
                </Typography>
                <Typography>
                    잔액: {(monthlyStats.totalIncome - monthlyStats.totalExpense)?.toLocaleString()}원
                </Typography>
            </Box>
        </Paper>
    );

    return (
        <Container maxWidth="lg" sx={{ py: 4 }}>  {/* 상하 패딩 추가 */}
            <Grid 
                container 
                spacing={4}  // 그리드 아이템 간격 증가
                sx={{ mb: 4 }}  // 하단 여백 추가
            >
                {/* 섹션 1: 월별 통계 */}
                <Grid item xs={12} md={6}>
                    <Paper 
                        elevation={3}  // 그림자 효과 추가
                        sx={{
                            p: 4,
                            height: '83%',
                            minHeight: 300,
                            display: 'flex',
                            flexDirection: 'column',
                            backgroundColor: 'background.paper',
                            borderRadius: 2
                        }}
                    >
                        <Typography variant="h6" gutterBottom>월별 통계</Typography>
                        <Box sx={{ flex: 1 }}>
                            <DatePicker
                                views={['year', 'month']}
                                value={selectedDate}
                                onChange={setSelectedDate}
                                sx={{ mb: 2 }}
                            />
                            <Typography>수입: {monthlyStats.totalIncome.toLocaleString()}원</Typography>
                            <Typography>지출: {monthlyStats.totalExpense.toLocaleString()}원</Typography>
                            <Typography>잔액: {monthlyStats.balance.toLocaleString()}원</Typography>
                        </Box>
                    </Paper>
                </Grid>

                {/* 섹션 2: 입력 폼 */}
                <Grid item xs={12} md={6}>
                    <Paper 
                        elevation={3}
                        sx={{
                            p: 4,
                            height: '83%',
                            minHeight: 250,
                            display: 'flex',
                            flexDirection: 'column',
                            backgroundColor: 'background.paper',
                            borderRadius: 2
                        }}
                    >
                        <Typography variant="h6" gutterBottom>수입/지출 입력</Typography>
                        {/* 입력 폼에 카테고리와 설명 필드 추가 */}
                        <form onSubmit={handleSubmit} style={{ flex: 1 }}>
                            <Box sx={{ mb: 2 }}>
                                <Button
                                    variant={formData.isIncome ? "contained" : "outlined"}
                                    color="success"
                                    onClick={() => handleIncomeToggle(true)}
                                    sx={{ mr: 1 }}
                                >
                                    수입
                                </Button>
                                <Button
                                    variant={!formData.isIncome ? "contained" : "outlined"}
                                    color="error"
                                    onClick={() => handleIncomeToggle(false)}
                                >
                                    지출
                                </Button>
                            </Box>

                            {/* 금액 입력 */}
                            <TextField
                                fullWidth
                                label="금액"
                                type="number"
                                value={formData.amount}
                                onChange={(e) => setFormData({...formData, amount: e.target.value})}
                                sx={{ mb: 2 }}
                            />

                            {/* 카테고리 선택 */}
                            <Select
                                fullWidth
                                value={formData.category}
                                onChange={(e) => setFormData({...formData, category: e.target.value})}
                                sx={{ mb: 2 }}
                            >
                                {currentCategories.map(cat => (
                                    <MenuItem key={cat.value} value={cat.value}>
                                        {cat.label}
                                    </MenuItem>
                                ))}
                            </Select>

                            {/* 설명 입력 */}
                            <TextField
                                fullWidth
                                label="설명"
                                multiline
                                rows={2}
                                value={formData.description}
                                onChange={(e) => setFormData({...formData, description: e.target.value})}
                                sx={{ mb: 2 }}
                            />

                            <Button 
                                type="submit" 
                                variant="contained" 
                                fullWidth
                                color={formData.isIncome ? "success" : "primary"}
                            >
                                저장
                            </Button>
                        </form>
                    </Paper>
                </Grid>

                {/* 섹션 3: 달력 */}
                <Grid item xs={12} md={6}>
                    <Paper 
                        elevation={3}
                        sx={{
                            p: 4,
                            height: '70%',
                            minHeight: 250,
                            display: 'flex',
                            flexDirection: 'column',
                            backgroundColor: 'background.paper',
                            borderRadius: 2
                        }}
                    >
                        <Typography variant="h6" gutterBottom>달력</Typography>
                        <Box sx={{
                            flex: 1,
                            '& .MuiDateCalendar-root': { width: '100%', height: '90%' }
                        }}>
                            <DateCalendar
                                value={selectedDate}
                                onChange={handleDateClick}
                                renderDay={renderDay}
                            />
                        </Box>
                    </Paper>
                </Grid>

                {/* 섹션 4: 상세 내역 */}
                <Grid item xs={12} md={6}>
                    <Paper 
                        elevation={3}
                        sx={{
                            p: 4,
                            height: '70%',
                            minHeight: 250,
                            display: 'flex',
                            flexDirection: 'column',
                            backgroundColor: 'background.paper',
                            borderRadius: 2,
                            overflow: 'auto'
                        }}
                    >
                        <Typography variant="h6" gutterBottom>
                            {selectedDate.format('YYYY년 MM월 DD일')} 내역
                        </Typography>
                        {renderDailyTransactions()}
                    </Paper>
                </Grid>
            </Grid>
            {editLedger && (
                <div>
                    <h2>수정</h2>
                    <input
                        type="number"
                        value={editLedger.amount}
                        onChange={(e) => setEditLedger({ ...editLedger, amount: e.target.value })}
                    />
                    <input
                        type="text"
                        value={editLedger.description}
                        onChange={(e) => setEditLedger({ ...editLedger, description: e.target.value })}
                    />
                    <button onClick={handleUpdate}>저장</button>
                    <button onClick={handleCancelEdit}>취소</button>
                </div>
            )}
        </Container>
    );
};

export default Ledgers;