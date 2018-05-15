package com.Sergio.EasyRMT.UnitTests.Service;

import com.Sergio.EasyRMT.Domain.CommentDom;
import com.Sergio.EasyRMT.Domain.UserDom;
import com.Sergio.EasyRMT.Model.Comment;
import com.Sergio.EasyRMT.Model.ObjectEntity;
import com.Sergio.EasyRMT.Model.User;
import com.Sergio.EasyRMT.Repository.CommentRepository;
import com.Sergio.EasyRMT.Repository.ObjectRepository;
import com.Sergio.EasyRMT.Repository.UserRepository;
import com.Sergio.EasyRMT.Service.CommentService;
import com.Sergio.EasyRMT.Service.Converter.CommentConverter;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.*;

@RunWith(SpringJUnit4ClassRunner.class)
public class CommentServiceTest {
    @Mock
    UserRepository userRepository;
    @Mock
    ObjectRepository objectRepository;
    @Mock
    CommentConverter commentConverter;
    @Mock
    CommentRepository commentRepository;

    @BeforeEach
    public void initMocks(){
        objectRepository = mock(ObjectRepository.class);
        userRepository = mock(UserRepository.class);
        commentConverter = mock(CommentConverter.class);
        commentRepository = mock(CommentRepository.class);
    }

    @Test
    @DisplayName("Get a list of comments")
    public void getComments_ObjectIdProvided_ListCommentDomReturned(){
        List<Comment> commentList = mock(List.class);
        List<CommentDom> expected = mock(List.class);
        when(commentRepository.findByObjectId(anyInt())).thenReturn(commentList);
        when(commentConverter.toDomain(commentList)).thenReturn(expected);

        CommentService commentService = createCommentService();

        List<CommentDom> obtained = commentService.getComments(1);

        assertNotNull(obtained);
        assertEquals(expected,obtained);
        verify(commentRepository, times(1)).findByObjectId(1);
        verify(commentConverter, times(1)).toDomain(commentList);

    }

    @Test
    @DisplayName("Create a comment returns a successful creation")
    public void createComment_CommentDomProvided_CommentCreated_TrueReturned(){
        CommentDom commentDom = new CommentDom();
        UserDom userDom = new UserDom();
        userDom.setUserId(1);
        commentDom.setUser(userDom);
        ObjectEntity objectEntity = new ObjectEntity();
        objectEntity.setIdobject(1);
        commentDom.setObject(objectEntity);
        commentDom.setComment("Comment");
        Comment comment = new Comment();
        comment.setComment("Comment");
        User user = new User();
        user.setUserId(1);
        when(commentConverter.toModel(commentDom)).thenReturn(comment);
        when(userRepository.findOne(1)).thenReturn(user);
        when(objectRepository.findOne(1)).thenReturn(objectEntity);
        when(commentRepository.save(any(Comment.class))).thenReturn(mock(Comment.class));

        CommentService commentService = createCommentService();

        boolean expected = true;
        boolean obtained = commentService.createComment(commentDom);

        assertNotNull(obtained);
        assertEquals(expected,obtained);
        verify(commentConverter,times(1)).toModel(commentDom);
        verify(userRepository, times(1)).findOne(1);
        verify(objectRepository, times(1)).findOne(1);
        verify(commentRepository, times(1)).save(any(Comment.class));
    }


    @Test
    @DisplayName("Create a comment returns a failed creation")
    public void createComment_CommentDomProvided_CommentCreated_falseReturned(){
        CommentDom commentDom = new CommentDom();
        UserDom userDom = new UserDom();
        userDom.setUserId(1);
        commentDom.setUser(userDom);
        ObjectEntity objectEntity = new ObjectEntity();
        objectEntity.setIdobject(1);
        commentDom.setObject(objectEntity);
        commentDom.setComment("Comment");
        Comment comment = new Comment();
        comment.setComment("Comment");
        User user = new User();
        user.setUserId(1);
        when(commentConverter.toModel(commentDom)).thenReturn(comment);
        when(userRepository.findOne(1)).thenReturn(user);
        when(objectRepository.findOne(1)).thenReturn(objectEntity);
        when(commentRepository.save(any(Comment.class))).thenThrow(Exception.class);

        CommentService commentService = createCommentService();

        boolean expected = false;
        boolean obtained = commentService.createComment(commentDom);

        assertNotNull(obtained);
        assertEquals(expected,obtained);
        verify(commentConverter,times(1)).toModel(commentDom);
        verify(userRepository, times(1)).findOne(1);
        verify(objectRepository, times(1)).findOne(1);
        verify(commentRepository, times(1)).save(any(Comment.class));
    }

    @Test
    @DisplayName("updateComment updates a comment successfully")
    public void updateComment_CommentDomAndUserProvided_CommentUpdated_TrueReturned(){
        CommentDom commentDom = new CommentDom();
        UserDom userDom = new UserDom();
        userDom.setUserId(1);
        commentDom.setUser(userDom);
        commentDom.setIdComment(1);
        commentDom.setComment("Comment");
        Comment comment = new Comment();
        comment.setComment("Comment");
        comment.setIdComment(1);
        User user = new User();
        user.setUserId(1);
        comment.setUser(user);
        when(commentRepository.findOne(1)).thenReturn(comment);
        when(commentRepository.save(any(Comment.class))).thenReturn(mock(Comment.class));
        CommentService commentService = createCommentService();

        boolean expected = true;
        boolean obtained = commentService.updateComment(commentDom,userDom);

        assertNotNull(obtained);
        assertEquals(expected,obtained);
        verify(commentRepository,times(1)).findOne(1);
        verify(commentRepository, times(1)).save(comment);
    }

    @Test
    @DisplayName("updateComment userId does not match")
    public void updateComment_CommentDomAndUserProvided_UsersIDnotMatch_FalseReturned(){
        CommentDom commentDom = new CommentDom();
        UserDom userDom = new UserDom();
        userDom.setUserId(5);
        commentDom.setUser(userDom);
        commentDom.setIdComment(1);
        commentDom.setComment("Comment");
        Comment comment = new Comment();
        comment.setComment("Comment");
        comment.setIdComment(1);
        User user = new User();
        user.setUserId(1);
        comment.setUser(user);
        when(commentRepository.findOne(1)).thenReturn(comment);
        when(commentRepository.save(any(Comment.class))).thenReturn(mock(Comment.class));
        CommentService commentService = createCommentService();

        boolean expected = false;
        boolean obtained = commentService.updateComment(commentDom,userDom);

        assertNotNull(obtained);
        assertEquals(expected,obtained);
        verify(commentRepository,times(1)).findOne(1);
        verify(commentRepository, times(0)).save(comment);
    }

    @Test
    @DisplayName("DeleteComment deletes a comment succesfully")
    public void deleteComment_CommentIdAndUserDomProvided_CommentDeleted_TrueReturned(){
        UserDom userDom = new UserDom();
        userDom.setUserId(1);
        Comment comment = mock(Comment.class);
        User user = new User();
        user.setUserId(1);
        when(commentRepository.exists(1)).thenReturn(true).thenReturn(false);
        when(commentRepository.findOne(1)).thenReturn(comment);
        when(comment.getUser()).thenReturn(user);
        doNothing().when(commentRepository).delete(1);

        boolean expected = true;

        CommentService commentService = createCommentService();

        boolean obtained = commentService.deleteComment(1,userDom);

        assertNotNull(obtained);
        assertEquals(expected,obtained);
        verify(commentRepository,times(2)).exists(1);
        verify(commentRepository,times(1)).delete(1);
        verify(commentRepository,times(1)).findOne(1);
    }

    @Test
    @DisplayName("DeleteComment fail to delete, users does not match")
    public void deleteComment_CommentIdAndUserDomProvided_UsersNotMatch_FalseReturned(){
        UserDom userDom = new UserDom();
        userDom.setUserId(1);
        Comment comment = mock(Comment.class);
        User user = new User();
        user.setUserId(5);
        when(commentRepository.exists(1)).thenReturn(true);
        when(commentRepository.findOne(1)).thenReturn(comment);
        when(comment.getUser()).thenReturn(user);

        boolean expected = false;

        CommentService commentService = createCommentService();

        boolean obtained = commentService.deleteComment(1,userDom);

        assertNotNull(obtained);
        assertEquals(expected,obtained);
        verify(commentRepository,times(1)).exists(1);
        verify(commentRepository,times(0)).delete(1);
        verify(commentRepository,times(1)).findOne(1);
    }

    @Test
    @DisplayName("DeleteComment fail to delete")
    public void deleteComment_CommentIdAndUserDomProvided_FailToDelete_FalseReturned(){
        UserDom userDom = new UserDom();
        userDom.setUserId(1);
        Comment comment = mock(Comment.class);
        User user = new User();
        user.setUserId(1);
        when(commentRepository.exists(1)).thenReturn(true).thenReturn(true);
        when(commentRepository.findOne(1)).thenReturn(comment);
        when(comment.getUser()).thenReturn(user);
        doNothing().when(commentRepository).delete(1);

        boolean expected = false;

        CommentService commentService = createCommentService();

        boolean obtained = commentService.deleteComment(1,userDom);

        assertNotNull(obtained);
        assertEquals(expected,obtained);
        verify(commentRepository,times(2)).exists(1);
        verify(commentRepository,times(1)).delete(1);
        verify(commentRepository,times(1)).findOne(1);
    }

    @Test
    @DisplayName("DeleteComment fail to delete, comment does not exists")
    public void deleteComment_CommentIdAndUserDomProvided_CommentNotExists_FalseReturned(){
        UserDom userDom = new UserDom();
        userDom.setUserId(1);
        Comment comment = mock(Comment.class);
        User user = new User();
        user.setUserId(1);
        when(commentRepository.exists(1)).thenReturn(false);

        boolean expected = false;

        CommentService commentService = createCommentService();

        boolean obtained = commentService.deleteComment(1,userDom);

        assertNotNull(obtained);
        assertEquals(expected,obtained);
        verify(commentRepository,times(1)).exists(1);
        verify(commentRepository,times(0)).delete(1);
        verify(commentRepository,times(0)).findOne(1);
    }

    private CommentService createCommentService(){
        return new CommentService(userRepository,objectRepository,
                commentConverter,commentRepository);
    }
}
