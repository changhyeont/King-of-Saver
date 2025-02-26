// src/components/Posts.js
import React, { useState, useEffect } from 'react';
import { Container, Typography, Card, CardContent, Grid } from '@mui/material';
import axios from 'axios';

const Posts = () => {
    const [posts, setPosts] = useState([]);

    useEffect(() => {
        const fetchPosts = async () => {
            try {
                const response = await axios.get('http://localhost:8080/api/posts', {
                    headers: {
                        Authorization: `Bearer ${localStorage.getItem('token')}`
                    }
                });
                setPosts(response.data);
            } catch (error) {
                console.error('Failed to fetch posts:', error);
            }
        };

        fetchPosts();
    }, []);

    return (
        <Container maxWidth="md" sx={{ mt: 4 }}>
            <Typography variant="h4" component="h1" gutterBottom>
                게시물 목록
            </Typography>
            <Grid container spacing={2}>
                {posts.map((post) => (
                    <Grid item xs={12} key={post.id}>
                        <Card>
                            <CardContent>
                                <Typography variant="h6">{post.title}</Typography>
                                <Typography color="text.secondary">{post.content}</Typography>
                            </CardContent>
                        </Card>
                    </Grid>
                ))}
            </Grid>
        </Container>
    );
};

export default Posts;