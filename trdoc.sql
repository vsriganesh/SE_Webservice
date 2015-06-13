create table TrDocument (TrID INTEGER PRIMARY KEY AUTO_INCREMENT, AuthorID INTEGER,FOREIGN KEY fk_authid(AuthorID) references Author(AuthorId) ,CreationDate DATE , ModifyDate DATE,StateID INTEGER, FOREIGN KEY fk_state(StateID) references State(StateID) , Description VARCHAR(256) , ReviewerCount INTEGER , CurrentCount INTEGER);


create table Author (AuthorID INTEGER PRIMARY KEY AUTO_INCREMENT , UserID INTEGER, FOREIGN KEY fk_auth(UserID) references User(UserID));


create table User (UserID INTEGER PRIMARY KEY AUTO_INCREMENT,UserName varchar(256),UserEmail varchar(256),Password varchar(256),Role varchar(256));


create table Reviewer (ReviewerID INTEGER PRIMARY KEY AUTO_INCREMENT , UserID INTEGER, FOREIGN KEY fk_rev(UserID) references User(UserID));



create table State (StateID INTEGER PRIMARY KEY  , StateName varchar(256));


create table doc_review (DocRevID INTEGER PRIMARY KEY AUTO_INCREMENT , StateID INTEGER,FOREIGN KEY fk_state_doc(StateID) references State(StateID),ReviewerID INTEGER,FOREIGN KEY fk_rev_doc(ReviewerID) references Reviewer(ReviewerID),TrID INTEGER,FOREIGN KEY fk_tr_doc(TrID) references trDocument(TrID));


create table Comment (CommentID INTEGER PRIMARY KEY AUTO_INCREMENT , CommentDesc varchar(1000) , CommentDate DATETIME, DocRevID INTEGER,FOREIGN KEY fk_co_doc(DocRevID) references doc_review(DocRevID));


create table Reviewer_Task (TrID INTEGER,FOREIGN KEY fk_revtask_tr(TrID) references trDocument(TrID),RevTaskID INTEGER PRIMARY KEY AUTO_INCREMENT,ReviewerID INTEGER,FOREIGN KEY fk_revtask_rev(ReviewerID) references Reviewer(ReviewerID));


create table Notification_Review_Task (NotificationRevTaskID INTEGER PRIMARY KEY AUTO_INCREMENT , RevTaskID INTEGER ,FOREIGN KEY fk_notrevtask_tr(RevTaskID) references Reviewer_Task(RevTaskID),Message varchar(1000) , NotificationDate DATETIME);


create table Notification_Doc_Review (NotificationDocReviewID INTEGER PRIMARY KEY AUTO_INCREMENT , DocRevID INTEGER ,FOREIGN KEY fk_notrevtask_docrev(DocRevID) references doc_review(DocRevID),Message varchar(1000) , NotificationDate DATETIME);



create table doc_auth (DocAuthID INTEGER PRIMARY KEY AUTO_INCREMENT,AuthorID INTEGER,FOREIGN KEY fk_author_doc(AuthorID) references Author(AuthorID),TrID INTEGER,FOREIGN KEY fk_trauthor_doc(TrID) references trDocument(TrID));

SHOW CREATE TABLE region;








