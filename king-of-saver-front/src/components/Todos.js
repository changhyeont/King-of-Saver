// src/components/Todos.js
import React, { useState, useEffect } from 'react';
import { Container, Typography, List, ListItem, ListItemText, Checkbox, TextField, Button } from '@mui/material';
import axios from 'axios';

const Todos = () => {
    const [todos, setTodos] = useState([]);
    const [newTodo, setNewTodo] = useState('');

    useEffect(() => {
        fetchTodos();
    }, []);

    const fetchTodos = async () => {
        try {
            const response = await axios.get('http://localhost:8080/api/todos', {
                headers: { Authorization: `Bearer ${localStorage.getItem('token')}` }
            });
            setTodos(response.data);
        } catch (error) {
            console.error('Failed to fetch todos:', error);
        }
    };

    const handleAddTodo = async () => {
        try {
            await axios.post('http://localhost:8080/api/todos',
                { content: newTodo },
                { headers: { Authorization: `Bearer ${localStorage.getItem('token')}` } }
            );
            setNewTodo('');
            fetchTodos();
        } catch (error) {
            console.error('Failed to add todo:', error);
        }
    };

    return (
        <Container maxWidth="md">
            <Typography variant="h4" gutterBottom>할 일 목록</Typography>
            <TextField
                fullWidth
                value={newTodo}
                onChange={(e) => setNewTodo(e.target.value)}
                placeholder="새로운 할 일"
            />
            <Button variant="contained" onClick={handleAddTodo}>추가</Button>
            <List>
                {todos.map(todo => (
                    <ListItem key={todo.id}>
                        <ListItemText primary={todo.content} />
                    </ListItem>
                ))}
            </List>
        </Container>
    );
};

export default Todos;