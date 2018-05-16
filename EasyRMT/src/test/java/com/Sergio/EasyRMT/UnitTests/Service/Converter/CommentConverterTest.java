/*
 * Copyright (c) $today.year.Sergio López Jiménez and Universidad de Valladolid
 *                             All rights reserved
 */

package com.Sergio.EasyRMT.UnitTests.Service.Converter;


import com.Sergio.EasyRMT.Domain.CommentDom;
import com.Sergio.EasyRMT.Domain.UserDom;
import com.Sergio.EasyRMT.Model.Comment;
import com.Sergio.EasyRMT.Model.ObjectEntity;
import com.Sergio.EasyRMT.Model.User;
import com.Sergio.EasyRMT.Service.Converter.CommentConverter;
import com.Sergio.EasyRMT.Service.Converter.UserConverter;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(SpringJUnit4ClassRunner.class)
public class CommentConverterTest {
    @Mock
    UserConverter userConverter;

    @BeforeEach
    public void initMocks(){
        userConverter = mock(UserConverter.class);
    }

    @Test
    @DisplayName("method to domain converts a list of comments in a list of commentsDom")
    public void toDomain_CommentListProvided_CommentDomListReturned(){

        ObjectEntity objectEntity = mock(ObjectEntity.class);
        Comment comment = mock(Comment.class);
        User user = mock(User.class);
        UserDom userDom = mock(UserDom.class);
        Date date = new Date();
        List<Comment> commentList = new ArrayList<>();
        commentList.add(comment);
        when(userConverter.toDomain(user)).thenReturn(userDom);
        when(comment.getUser()).thenReturn(user);
        when(comment.getComment()).thenReturn("Mock Comment");
        when(comment.getCreated()).thenReturn(date);
        when(comment.getLastModified()).thenReturn(date);
        when(comment.getObject()).thenReturn(objectEntity);

        CommentConverter commentConverter = createCommentConverter();
        List<CommentDom> commentDomList = commentConverter.toDomain(commentList);

        assertFalse(commentDomList.isEmpty());
        assertEquals(commentDomList.size(),1);
        assertNotNull(commentDomList);
        verify(userConverter, times(1)).toDomain(user);
        verify(comment, times(1)).getUser();
        verify(comment, times(1)).getIdComment();
        verify(comment, times(1)).getComment();
        verify(comment, times(1)).getCreated();
        verify(comment, times(1)).getLastModified();
        verify(comment, times(1)).getObject();
    }

    @Test
    @DisplayName("method to domain converts a comment in a commentDom")
    public void toDomain_CommentProvided_CommentDomReturned(){

        ObjectEntity objectEntity = mock(ObjectEntity.class);
        Comment comment = mock(Comment.class);
        User user = mock(User.class);
        UserDom userDom = mock(UserDom.class);
        Date date = new Date();
        when(userConverter.toDomain(user)).thenReturn(userDom);
        when(comment.getUser()).thenReturn(user);
        when(comment.getComment()).thenReturn("Mock Comment");
        when(comment.getCreated()).thenReturn(date);
        when(comment.getLastModified()).thenReturn(date);
        when(comment.getObject()).thenReturn(objectEntity);

        CommentConverter commentConverter = createCommentConverter();
        CommentDom commentDom = commentConverter.toDomain(comment);

        assertNotNull(commentDom);
        verify(userConverter, times(1)).toDomain(user);
        verify(comment, times(1)).getUser();
        verify(comment, times(1)).getIdComment();
        verify(comment, times(1)).getComment();
        verify(comment, times(1)).getCreated();
        verify(comment, times(1)).getLastModified();
        verify(comment, times(1)).getObject();
    }

    @Test
    @DisplayName("method to domain converts a  commentDom into a comment")
    public void toModel_CommentDomProvided_CommentReturned(){

        CommentDom commentDom = mock(CommentDom.class);
        Date date = new Date();
        when(commentDom.getComment()).thenReturn("Mock Comment");
        when(commentDom.getCreated()).thenReturn(date);
        when(commentDom.getLastModified()).thenReturn(date);

        CommentConverter commentConverter = createCommentConverter();
        Comment comment = commentConverter.toModel(commentDom);

        assertNotNull(comment);
        verify(userConverter, times(0)).toModel(any(UserDom.class));
        verify(commentDom, times(0)).getUser();
        verify(commentDom, times(0)).getIdComment();
        verify(commentDom, times(1)).getComment();
        verify(commentDom, times(1)).getCreated();
        verify(commentDom, times(1)).getLastModified();
        verify(commentDom, times(0)).getObject();
    }

    private CommentConverter createCommentConverter(){
        return new CommentConverter(userConverter);
    }
}
