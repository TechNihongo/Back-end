CREATE DATABASE TechNihongo_V1

USE TechNihongo_V1

CREATE TABLE [AuthToken] (
  [token_id] INT PRIMARY KEY IDENTITY(1, 1),
  [user_id] INT,
  [token] NVARCHAR(255),
  [token_type] NVARCHAR(50),
  [expires_at] DATETIME2,
  [created_at] DATETIME2 DEFAULT GETDATE(),
  [is_active] BIT DEFAULT 1
)

CREATE TABLE [Role] (
  [role_id] INT PRIMARY KEY IDENTITY(1, 1),
  [rolename] NVARCHAR(50)
)

CREATE TABLE [User] (
  [user_id] INT PRIMARY KEY IDENTITY(1, 1),
  [username] NVARCHAR(50) UNIQUE,
  [email] NVARCHAR(100) UNIQUE,
  [password] NVARCHAR(255),
  [uid] NVARCHAR(255) UNIQUE,
  [is_active] BIT DEFAULT 1,
  [created_at] DATETIME2 DEFAULT GETDATE(),
  [last_login] DATETIME2,
  [role_id] INT DEFAULT 0,
  [profile_img] NVARCHAR(255)
)

CREATE TABLE [Student] (
  [student_id] INT PRIMARY KEY IDENTITY(1, 1),
  [user_id] INT,
  [bio] NVARCHAR(MAX),
  [daily_goal_minutes] INT DEFAULT 60,
  [reminder_enabled] BIT DEFAULT 0,
  [reminder_time] TIME,
  [updated_at] DATETIME2 DEFAULT GETDATE()
)

CREATE TABLE [FlashCard] (
  [vocab_id] INT PRIMARY KEY IDENTITY(1, 1),
  [set_id] INT,
  [japanese_definition] NVARCHAR(MAX),
  [viet_eng_translation] NVARCHAR(MAX),
  [image_url] NVARCHAR(255),
  [vocab_order] INT,
  [created_at] DATETIME2 DEFAULT GETDATE(),
  [last_updated] DATETIME2 DEFAULT GETDATE()
)

CREATE TABLE [StudentFlashCardSet] (
  [set_id] INT PRIMARY KEY IDENTITY(1, 1),
  [title] NVARCHAR(255),
  [description] NVARCHAR(MAX),
  [domain_id] INT,
  [difficulty_level_id] INT,
  [creator_id] INT,
  [resource_id] INT,
  [is_public] BIT DEFAULT 0,
  [created_at] DATETIME2 DEFAULT GETDATE(),
  [updated_at] DATETIME2 DEFAULT GETDATE()
)

CREATE TABLE [SystemFlashCardSet] (
  [set_id] INT PRIMARY KEY IDENTITY(1, 1),
  [title] NVARCHAR(255),
  [description] NVARCHAR(MAX),
  [domain_id] INT,
  [difficulty_level_id] INT,
  [is_public] BIT DEFAULT 0,
  [is_premium] BIT DEFAULT 0,
  [created_at] DATETIME2 DEFAULT GETDATE(),
  [updated_at] DATETIME2 DEFAULT GETDATE()
)

CREATE TABLE [StudentAchievement] (
  [student_achievement_id] INT PRIMARY KEY IDENTITY(1, 1),
  [student_id] INT,
  [achievement_id] INT,
  [achieved_at] DATETIME2
)

CREATE TABLE [Achievement] (
  [achievement_id] INT PRIMARY KEY IDENTITY(1, 1),
  [code] NVARCHAR(50) UNIQUE,
  [badge_name] NVARCHAR(100),
  [description] NVARCHAR(MAX),
  [image_url] NVARCHAR(255),
  [category] NVARCHAR(50),
  [condition_type] NVARCHAR(50),
  [condition_value] INT,
  [is_active] BIT DEFAULT 1,
  [display_order] INT,
  [created_at] DATETIME2 DEFAULT GETDATE()
)

CREATE TABLE [StudentAchievementProgress] (
  [progress_id] INT PRIMARY KEY IDENTITY(1, 1),
  [student_id] INT,
  [achievement_id] INT,
  [current_value] INT DEFAULT 0,
  [required_value] INT,
  [last_updated] DATETIME2,
  [created_at] DATETIME2 DEFAULT GETDATE()
)

CREATE TABLE [StudentFolder] (
  [folder_id] INT PRIMARY KEY IDENTITY(1, 1),
  [student_id] INT,
  [name] NVARCHAR(100),
  [description] NVARCHAR(MAX),
  [created_at] DATETIME2 DEFAULT GETDATE(),
  [updated_at] DATETIME2 DEFAULT GETDATE()
)

CREATE TABLE [FolderItem] (
  [folder_item_id] INT PRIMARY KEY IDENTITY(1, 1),
  [folder_id] INT,
  [set_id] INT,
  [added_at] DATETIME2 DEFAULT GETDATE()
)

CREATE TABLE [Course] (
  [course_id] INT PRIMARY KEY IDENTITY(1, 1),
  [title] NVARCHAR(255),
  [description] NVARCHAR(MAX),
  [creator_id] INT,
  [domain_id] INT,
  [difficulty_level_id] INT,
  [attachment_url] NVARCHAR(255),
  [estimated_duration] INT,
  [is_public] BIT DEFAULT 0,
  [is_premium] BIT DEFAULT 0,
  [created_at] DATETIME2 DEFAULT GETDATE()
)

CREATE TABLE [Lesson] (
  [lesson_id] INT PRIMARY KEY IDENTITY(1, 1),
  [plan_id] INT,
  [title] NVARCHAR(255),
  [content] NVARCHAR(MAX),
  [lesson_order] INT,
  [is_prerequisite] BIT,
  [min_completion_percentage] DECIMAL(5,2) DEFAULT 100,
  [created_at] DATETIME2 DEFAULT GETDATE()
)

CREATE TABLE [LearningPath] (
  [path_id] INT PRIMARY KEY IDENTITY(1, 1),
  [title] NVARCHAR(255),
  [description] NVARCHAR(MAX),
  [domain_id] INT,
  [creator_id] INT,
  [is_public] BIT DEFAULT 0,
  [created_at] DATETIME2 DEFAULT GETDATE()
)

CREATE TABLE [PathCourse] (
  [path_course_id] INT PRIMARY KEY IDENTITY(1, 1),
  [path_id] INT,
  [course_id] INT,
  [course_order] INT
)

CREATE TABLE [SubscriptionPlan] (
  [plan_id] INT PRIMARY KEY IDENTITY(1, 1),
  [name] NVARCHAR(100),
  [description] NVARCHAR(MAX),
  [price] DECIMAL(10,2),
  [benifits] NVARCHAR(MAX),
  [duration_days] INT,
  [is_active] BIT DEFAULT 1,
  [create_at] DATETIME2 DEFAULT GETDATE()
)

CREATE TABLE [StudentSubscription] (
  [subscription_id] INT PRIMARY KEY IDENTITY(1, 1),
  [student_id] INT,
  [plan_id] INT,
  [start_date] DATE,
  [end_date] DATE,
  [is_active] BIT DEFAULT 1
)

CREATE TABLE [StudentCourseRating] (
  [rating_id] INT PRIMARY KEY IDENTITY(1, 1),
  [student_id] INT,
  [course_id] INT,
  [rating] DECIMAL(2,1),
  [is_verified] BIT DEFAULT 0,
  [created_at] DATETIME2 DEFAULT GETDATE(),
  [updated_at] DATETIME2 DEFAULT GETDATE()
)

CREATE TABLE [Quiz] (
  [quiz_id] INT PRIMARY KEY IDENTITY(1, 1),
  [title] NVARCHAR(255),
  [description] NVARCHAR(MAX),
  [quiz_type] NVARCHAR(50),
  [domain_id] INT,
  [creator_id] INT,
  [difficulty_level_id] INT,
  [time_limit] INT,
  [passing_score] DECIMAL(5,2),
  [is_public] BIT DEFAULT 0,
  [is_active] BIT DEFAULT 1,
  [created_at] DATETIME2 DEFAULT GETDATE(),
  [updated_at] DATETIME2 DEFAULT GETDATE()
)

CREATE TABLE [QuizQuestion] (
  [question_id] INT PRIMARY KEY IDENTITY(1, 1),
  [quiz_id] INT,
  [question_text] NVARCHAR(MAX),
  [question_type] NVARCHAR(50),
  [explanation] NVARCHAR(MAX),
  [points] INT DEFAULT 1,
  [question_order] INT,
  [is_required] BIT DEFAULT 1,
  [media_url] NVARCHAR(255),
  [created_at] DATETIME2 DEFAULT GETDATE(),
  [updated_at] DATETIME2 DEFAULT GETDATE()
)

CREATE TABLE [QuizAnswerOption] (
  [option_id] INT PRIMARY KEY IDENTITY(1, 1),
  [question_id] INT,
  [option_text] NVARCHAR(MAX),
  [is_correct] BIT DEFAULT 0,
  [explanation] NVARCHAR(MAX),
  [option_order] INT,
  [created_at] DATETIME2 DEFAULT GETDATE()
)

CREATE TABLE [StudentQuizAttempt] (
  [attempt_id] INT PRIMARY KEY IDENTITY(1, 1),
  [quiz_id] INT,
  [student_id] INT,
  [start_time] DATETIME2,
  [end_time] DATETIME2,
  [score] DECIMAL(5,2),
  [passing_score] DECIMAL(5,2),
  [time_taken] INT,
  [is_completed] BIT DEFAULT 0,
  [attempt_number] INT,
  [created_at] DATETIME2 DEFAULT GETDATE()
)

CREATE TABLE [QuizAnswerResponse] (
  [response_id] INT PRIMARY KEY IDENTITY(1, 1),
  [attempt_id] INT,
  [question_id] INT,
  [selected_option_id] INT,
  [text_response] NVARCHAR(MAX),
  [is_correct] BIT,
  [points_earned] DECIMAL(5,2),
  [response_time] INT,
  [created_at] DATETIME2 DEFAULT GETDATE()
)

CREATE TABLE [StudentVocabularyProgress] (
  [progress_id] INT PRIMARY KEY IDENTITY(1, 1),
  [student_id] INT,
  [vocab_id] INT,
  [is_learned] BIT DEFAULT 0,
  [last_studied] DATETIME2,
  [study_count] INT DEFAULT 0,
  [total_study_time] INT DEFAULT 0,
  [starred] BIT DEFAULT 0,
  [created_at] DATETIME2 DEFAULT GETDATE(),
  [updated_at] DATETIME2 DEFAULT GETDATE()
)

CREATE TABLE [StudentFlashCardSetProgress] (
  [progress_id] INT PRIMARY KEY IDENTITY(1, 1),
  [student_id] INT,
  [set_id] INT,
  [completion_percentage] DECIMAL(5,2) DEFAULT 0,
  [last_studied] DATETIME2,
  [study_count] INT DEFAULT 0,
  [total_study_time] INT DEFAULT 0,
  [created_at] DATETIME2 DEFAULT GETDATE(),
  [updated_at] DATETIME2 DEFAULT GETDATE()
)

CREATE TABLE [StudentCourseProgress] (
  [progress_id] INT PRIMARY KEY IDENTITY(1, 1),
  [student_id] INT,
  [course_id] INT,
  [completion_percentage] DECIMAL(5,2) DEFAULT 0,
  [completion_status] NVARCHAR(20) DEFAULT 'not_started',
  [current_lesson_id] INT,
  [completed_lessons] INT DEFAULT 0,
  [total_study_time] INT DEFAULT 0,
  [completed_date] DATETIME2,
  [created_at] DATETIME2 DEFAULT GETDATE(),
  [updated_at] DATETIME2 DEFAULT GETDATE()
)

CREATE TABLE [StudentLessonProgress] (
  [progress_id] INT PRIMARY KEY IDENTITY(1, 1),
  [student_id] INT,
  [lesson_id] INT,
  [completion_percentage] DECIMAL(5,2) DEFAULT 0,
  [completion_status] NVARCHAR(20) DEFAULT 'not_started',
  [last_studied] DATETIME2,
  [completed_resources] INT DEFAULT 0,
  [total_study_time] INT DEFAULT 0,
  [highest_quiz_score] DECIMAL(5,2),
  [notes] NVARCHAR(MAX)
)

CREATE TABLE [PaymentMethod] (
  [method_id] INT PRIMARY KEY IDENTITY(1, 1),
  [name] NVARCHAR(100),
  [code] NVARCHAR(50),
  [is_active] BIT DEFAULT 1,
  [payment_instructions] NVARCHAR(MAX),
  [created_at] DATETIME2 DEFAULT GETDATE()
)

CREATE TABLE [PaymentTransaction] (
  [transaction_id] INT PRIMARY KEY IDENTITY(1, 1),
  [subscription_id] INT,
  [method_id] INT,
  [final_amount] DECIMAL(10,2),
  [currency] NVARCHAR(3) DEFAULT 'VND',
  [payment_instructions] NVARCHAR(MAX),
  [expires_at] DATETIME2,
  [created_at] DATETIME2 DEFAULT GETDATE()
)

CREATE TABLE [PaymentHistory] (
  [history_id] INT PRIMARY KEY IDENTITY(1, 1),
  [transaction_id] INT,
  [status] NVARCHAR(20),
  [transaction_code] NVARCHAR(255),
  [admin_confirmed_by] INT,
  [admin_confirmed_at] DATETIME2,
  [note] NVARCHAR(MAX),
  [created_at] DATETIME2 DEFAULT GETDATE()
)

CREATE TABLE [UserActivityLog] (
  [log_id] INT PRIMARY KEY IDENTITY(1, 1),
  [user_id] INT,
  [activity_type] NVARCHAR(100),
  [content_type] NVARCHAR(50),
  [content_id] INT,
  [description] NVARCHAR(MAX),
  [ip_address] NVARCHAR(45),
  [created_at] DATETIME2 DEFAULT GETDATE()
)

CREATE TABLE [DifficultyLevel] (
  [level_id] INT PRIMARY KEY IDENTITY(1, 1),
  [code] NVARCHAR(20) UNIQUE,
  [name] NVARCHAR(100),
  [description] NVARCHAR(MAX),
  [order_sequence] INT,
  [is_active] BIT DEFAULT 1,
  [created_at] DATETIME2 DEFAULT GETDATE()
)

CREATE TABLE [Domain] (
  [domain_id] INT PRIMARY KEY IDENTITY(1, 1),
  [code] NVARCHAR(50) UNIQUE,
  [name] NVARCHAR(100),
  [description] NVARCHAR(MAX),
  [parent_domain_id] INT,
  [is_active] BIT DEFAULT 1,
  [created_at] DATETIME2 DEFAULT GETDATE()
)

CREATE TABLE [LearningResource] (
  [resource_id] INT PRIMARY KEY IDENTITY(1, 1),
  [title] NVARCHAR(255),
  [description] NVARCHAR(MAX),
  [resource_type_id] INT,
  [content_url] NVARCHAR(255),
  [domain_id] INT,
  [creator_id] INT,
  [is_premium] BIT DEFAULT 0,
  [is_public] BIT DEFAULT 1,
  [is_active] BIT DEFAULT 1,
  [created_at] DATETIME2 DEFAULT GETDATE(),
  [updated_at] DATETIME2 DEFAULT GETDATE()
)

CREATE TABLE [ResourceType] (
  [type_id] INT PRIMARY KEY IDENTITY(1, 1),
  [name] NVARCHAR(50),
  [description] NVARCHAR(MAX),
  [allowed_file_extensions] NVARCHAR(MAX),
  [is_active] BIT DEFAULT 1,
  [created_at] DATETIME2 DEFAULT GETDATE()
)

CREATE TABLE [LessonResource] (
  [lesson_resource_id] INT PRIMARY KEY IDENTITY(1, 1),
  [lesson_id] INT,
  [type] NVARCHAR(100),
  [type_order] INT,
  [resource_id] INT,
  [set_id] INT,
  [quiz_id] INT,
  [created_at] DATETIME2 DEFAULT GETDATE()
)

CREATE TABLE [StudentFavorite] (
  [favorite_id] INT PRIMARY KEY IDENTITY(1, 1),
  [student_id] INT,
  [resource_id] INT,
  [notes] NVARCHAR(MAX),
  [created_at] DATETIME2 DEFAULT GETDATE()
)

CREATE TABLE [StudentLearningStatistics] (
  [stat_id] INT PRIMARY KEY IDENTITY(1, 1),
  [student_id] INT,
  [total_study_time] INT DEFAULT 0,
  [total_completed_courses] INT DEFAULT 0,
  [total_completed_lessons] INT DEFAULT 0,
  [total_completed_quizes] INT DEFAULT 0,
  [active_days_count] INT DEFAULT 0,
  [total_achievements_unlocked] INT DEFAULT 0,
  [last_study_date] DATETIME2,
  [created_at] DATETIME2 DEFAULT GETDATE(),
  [updated_at] DATETIME2 DEFAULT GETDATE()
)

CREATE TABLE [StudentDailyLearningLog] (
  [log_id] INT PRIMARY KEY IDENTITY(1, 1),
  [student_id] INT,
  [log_date] DATE,
  [study_time] INT DEFAULT 0,
  [completed_lessons] INT DEFAULT 0,
  [completed_courses] INT DEFAULT 0,
  [quiz_attempts] INT DEFAULT 0,
  [points_earned] DECIMAL(5,2) DEFAULT 0,
  [created_at] DATETIME2 DEFAULT GETDATE()
)

CREATE TABLE [StudentQuizPerformanceStatistics] (
  [stat_id] INT PRIMARY KEY IDENTITY(1, 1),
  [student_id] INT,
  [quiz_id] INT,
  [total_attempts] INT DEFAULT 0,
  [highest_score] DECIMAL(5,2) DEFAULT 0,
  [average_score] DECIMAL(5,2) DEFAULT 0,
  [total_time_spent] INT DEFAULT 0,
  [last_attempt_date] DATETIME2,
  [created_at] DATETIME2 DEFAULT GETDATE()
)

CREATE TABLE [StudyPlan] (
  [plan_id] INT PRIMARY KEY IDENTITY(1, 1),
  [title] NVARCHAR(255),
  [description] NVARCHAR(MAX),
  [hours_per_day] INT,
  [total_months] INT,
  [is_active] BIT DEFAULT 1,
  [created_at] DATETIME2 DEFAULT GETDATE()
)

CREATE TABLE [CourseStudyPlan] (
  [course_plan_id] INT PRIMARY KEY IDENTITY(1, 1),
  [course_id] INT,
  [plan_id] INT,
  [lesson_count] INT,
  [created_at] DATETIME2 DEFAULT GETDATE()
)

CREATE TABLE [StudentStudyPlan] (
  [student_plan_id] INT PRIMARY KEY IDENTITY(1, 1),
  [student_id] INT,
  [course_id] INT,
  [course_plan_id] INT,
  [start_date] DATE,
  [status] NVARCHAR(20) DEFAULT 'active',
  [previous_plan_id] INT,
  [switch_date] DATETIME2,
  [created_at] DATETIME2 DEFAULT GETDATE(),
  [updated_at] DATETIME2 DEFAULT GETDATE()
)

CREATE TABLE [StudentLessonResourceProgress] (
  [progress_id] INT PRIMARY KEY IDENTITY(1, 1),
  [student_id] INT,
  [lesson_resource_id] INT,
  [completion_status] NVARCHAR(20) DEFAULT 'not_started',
  [is_completed] BIT,
  [last_accessed] DATETIME2,
  [total_time_spent] INT DEFAULT 0,
  [notes] NVARCHAR(MAX),
  [created_at] DATETIME2 DEFAULT GETDATE(),
  [updated_at] DATETIME2 DEFAULT GETDATE()
)

-- Foreign Key Constraints
ALTER TABLE [AuthToken] ADD CONSTRAINT [FK_AuthToken_User] 
    FOREIGN KEY ([user_id]) REFERENCES [User] ([user_id])

ALTER TABLE [User] ADD CONSTRAINT [FK_User_Role]
    FOREIGN KEY ([role_id]) REFERENCES [Role] ([role_id])

ALTER TABLE [Student] ADD CONSTRAINT [FK_Student_User]
    FOREIGN KEY ([user_id]) REFERENCES [User] ([user_id])

ALTER TABLE [FlashCard] ADD CONSTRAINT [FK_FlashCard_StudentSet]
    FOREIGN KEY ([set_id]) REFERENCES [StudentFlashCardSet] ([set_id])

ALTER TABLE [FlashCard] ADD CONSTRAINT [FK_FlashCard_SystemSet]
    FOREIGN KEY ([set_id]) REFERENCES [SystemFlashCardSet] ([set_id])

ALTER TABLE [StudentFlashCardSet] ADD CONSTRAINT [FK_StudentSet_Domain]
    FOREIGN KEY ([domain_id]) REFERENCES [Domain] ([domain_id])

ALTER TABLE [StudentFlashCardSet] ADD CONSTRAINT [FK_StudentSet_DifficultyLevel]
    FOREIGN KEY ([difficulty_level_id]) REFERENCES [DifficultyLevel] ([level_id])

ALTER TABLE [StudentFlashCardSet] ADD CONSTRAINT [FK_StudentSet_Student]
    FOREIGN KEY ([creator_id]) REFERENCES [Student] ([student_id])

ALTER TABLE [StudentFlashCardSet] ADD CONSTRAINT [FK_StudentSet_Resource]
    FOREIGN KEY ([resource_id]) REFERENCES [LearningResource] ([resource_id])

ALTER TABLE [SystemFlashCardSet] ADD CONSTRAINT [FK_SystemSet_Domain]
    FOREIGN KEY ([domain_id]) REFERENCES [Domain] ([domain_id])

ALTER TABLE [SystemFlashCardSet] ADD CONSTRAINT [FK_SystemSet_DifficultyLevel]
    FOREIGN KEY ([difficulty_level_id]) REFERENCES [DifficultyLevel] ([level_id])

ALTER TABLE [StudentAchievement] ADD CONSTRAINT [FK_StudentAchievement_Student]
    FOREIGN KEY ([student_id]) REFERENCES [Student] ([student_id])

ALTER TABLE [StudentAchievement] ADD CONSTRAINT [FK_StudentAchievement_Achievement]
    FOREIGN KEY ([achievement_id]) REFERENCES [Achievement] ([achievement_id])

ALTER TABLE [StudentAchievementProgress] ADD CONSTRAINT [FK_AchievementProgress_Student]
    FOREIGN KEY ([student_id]) REFERENCES [Student] ([student_id])

ALTER TABLE [StudentAchievementProgress] ADD CONSTRAINT [FK_AchievementProgress_Achievement]
    FOREIGN KEY ([achievement_id]) REFERENCES [Achievement] ([achievement_id])

ALTER TABLE [StudentFolder] ADD CONSTRAINT [FK_StudentFolder_Student]
    FOREIGN KEY ([student_id]) REFERENCES [Student] ([student_id])

ALTER TABLE [FolderItem] ADD CONSTRAINT [FK_FolderItem_Folder]
    FOREIGN KEY ([folder_id]) REFERENCES [StudentFolder] ([folder_id])

ALTER TABLE [FolderItem] ADD CONSTRAINT [FK_FolderItem_Set]
    FOREIGN KEY ([set_id]) REFERENCES [StudentFlashCardSet] ([set_id])

ALTER TABLE [Course] ADD CONSTRAINT [FK_Course_Creator]
    FOREIGN KEY ([creator_id]) REFERENCES [User] ([user_id])

ALTER TABLE [Course] ADD CONSTRAINT [FK_Course_Domain]
    FOREIGN KEY ([domain_id]) REFERENCES [Domain] ([domain_id])

ALTER TABLE [Course] ADD CONSTRAINT [FK_Course_DifficultyLevel]
    FOREIGN KEY ([difficulty_level_id]) REFERENCES [DifficultyLevel] ([level_id])

ALTER TABLE [Lesson] ADD CONSTRAINT [FK_Lesson_StudyPlan]
    FOREIGN KEY ([plan_id]) REFERENCES [StudyPlan] ([plan_id])

ALTER TABLE [LearningPath] ADD CONSTRAINT [FK_LearningPath_Domain]
    FOREIGN KEY ([domain_id]) REFERENCES [Domain] ([domain_id])

ALTER TABLE [LearningPath] ADD CONSTRAINT [FK_LearningPath_Creator]
    FOREIGN KEY ([creator_id]) REFERENCES [User] ([user_id])

ALTER TABLE [PathCourse] ADD CONSTRAINT [FK_PathCourse_Path]
    FOREIGN KEY ([path_id]) REFERENCES [LearningPath] ([path_id])

ALTER TABLE [PathCourse] ADD CONSTRAINT [FK_PathCourse_Course]
    FOREIGN KEY ([course_id]) REFERENCES [Course] ([course_id])
GO

ALTER TABLE [StudentSubscription] ADD FOREIGN KEY ([student_id]) REFERENCES [Student] ([student_id])
GO

ALTER TABLE [StudentSubscription] ADD FOREIGN KEY ([plan_id]) REFERENCES [SubscriptionPlan] ([plan_id])
GO

ALTER TABLE [StudentCourseRating] ADD FOREIGN KEY ([student_id]) REFERENCES [Student] ([student_id])
GO

ALTER TABLE [StudentCourseRating] ADD FOREIGN KEY ([course_id]) REFERENCES [Course] ([course_id])
GO

ALTER TABLE [Quiz] ADD FOREIGN KEY ([domain_id]) REFERENCES [Domain] ([domain_id])
GO

ALTER TABLE [Quiz] ADD FOREIGN KEY ([creator_id]) REFERENCES [User] ([user_id])
GO

ALTER TABLE [Quiz] ADD FOREIGN KEY ([difficulty_level_id]) REFERENCES [DifficultyLevel] ([level_id])
GO

ALTER TABLE [QuizQuestion] ADD FOREIGN KEY ([quiz_id]) REFERENCES [Quiz] ([quiz_id])
GO

ALTER TABLE [QuizAnswerOption] ADD FOREIGN KEY ([question_id]) REFERENCES [QuizQuestion] ([question_id])
GO

ALTER TABLE [StudentQuizAttempt] ADD FOREIGN KEY ([quiz_id]) REFERENCES [Quiz] ([quiz_id])
GO

ALTER TABLE [StudentQuizAttempt] ADD FOREIGN KEY ([student_id]) REFERENCES [Student] ([student_id])
GO

ALTER TABLE [QuizAnswerResponse] ADD FOREIGN KEY ([attempt_id]) REFERENCES [StudentQuizAttempt] ([attempt_id])
GO

ALTER TABLE [QuizAnswerResponse] ADD FOREIGN KEY ([question_id]) REFERENCES [QuizQuestion] ([question_id])
GO

ALTER TABLE [QuizAnswerResponse] ADD FOREIGN KEY ([selected_option_id]) REFERENCES [QuizAnswerOption] ([option_id])
GO

ALTER TABLE [StudentVocabularyProgress] ADD FOREIGN KEY ([student_id]) REFERENCES [Student] ([student_id])
GO

ALTER TABLE [StudentVocabularyProgress] ADD FOREIGN KEY ([vocab_id]) REFERENCES [FlashCard] ([vocab_id])
GO

ALTER TABLE [StudentFlashCardSetProgress] ADD FOREIGN KEY ([student_id]) REFERENCES [Student] ([student_id])
GO

ALTER TABLE [StudentFlashCardSetProgress] ADD FOREIGN KEY ([set_id]) REFERENCES [StudentFlashCardSet] ([set_id])
GO

ALTER TABLE [StudentFlashCardSetProgress] ADD FOREIGN KEY ([set_id]) REFERENCES [SystemFlashCardSet] ([set_id])
GO

ALTER TABLE [StudentCourseProgress] ADD FOREIGN KEY ([student_id]) REFERENCES [Student] ([student_id])
GO

ALTER TABLE [StudentCourseProgress] ADD FOREIGN KEY ([course_id]) REFERENCES [Course] ([course_id])
GO

ALTER TABLE [StudentCourseProgress] ADD FOREIGN KEY ([current_lesson_id]) REFERENCES [Lesson] ([lesson_id])
GO

ALTER TABLE [StudentLessonProgress] ADD FOREIGN KEY ([student_id]) REFERENCES [Student] ([student_id])
GO

ALTER TABLE [StudentLessonProgress] ADD FOREIGN KEY ([lesson_id]) REFERENCES [Lesson] ([lesson_id])
GO

ALTER TABLE [PaymentTransaction] ADD FOREIGN KEY ([subscription_id]) REFERENCES [StudentSubscription] ([subscription_id])
GO

ALTER TABLE [PaymentTransaction] ADD FOREIGN KEY ([method_id]) REFERENCES [PaymentMethod] ([method_id])
GO

ALTER TABLE [PaymentHistory] ADD FOREIGN KEY ([transaction_id]) REFERENCES [PaymentTransaction] ([transaction_id])
GO

ALTER TABLE [PaymentHistory] ADD FOREIGN KEY ([admin_confirmed_by]) REFERENCES [User] ([user_id])
GO

ALTER TABLE [UserActivityLog] ADD FOREIGN KEY ([user_id]) REFERENCES [User] ([user_id])
GO

ALTER TABLE [Domain] ADD FOREIGN KEY ([parent_domain_id]) REFERENCES [Domain] ([domain_id])
GO

ALTER TABLE [LearningResource] ADD FOREIGN KEY ([resource_type_id]) REFERENCES [ResourceType] ([type_id])
GO

ALTER TABLE [LearningResource] ADD FOREIGN KEY ([domain_id]) REFERENCES [Domain] ([domain_id])
GO

ALTER TABLE [LearningResource] ADD FOREIGN KEY ([creator_id]) REFERENCES [User] ([user_id])
GO

ALTER TABLE [LessonResource] ADD FOREIGN KEY ([lesson_id]) REFERENCES [Lesson] ([lesson_id])
GO

ALTER TABLE [LessonResource] ADD FOREIGN KEY ([resource_id]) REFERENCES [LearningResource] ([resource_id])
GO

ALTER TABLE [LessonResource] ADD FOREIGN KEY ([set_id]) REFERENCES [SystemFlashCardSet] ([set_id])
GO

ALTER TABLE [LessonResource] ADD FOREIGN KEY ([quiz_id]) REFERENCES [Quiz] ([quiz_id])
GO

ALTER TABLE [StudentFavorite] ADD FOREIGN KEY ([student_id]) REFERENCES [Student] ([student_id])
GO

ALTER TABLE [StudentFavorite] ADD FOREIGN KEY ([resource_id]) REFERENCES [LearningResource] ([resource_id])
GO

ALTER TABLE [StudentLearningStatistics] ADD FOREIGN KEY ([student_id]) REFERENCES [Student] ([student_id])
GO

ALTER TABLE [StudentDailyLearningLog] ADD FOREIGN KEY ([student_id]) REFERENCES [Student] ([student_id])
GO

ALTER TABLE [StudentQuizPerformanceStatistics] ADD FOREIGN KEY ([student_id]) REFERENCES [Student] ([student_id])
GO

ALTER TABLE [StudentQuizPerformanceStatistics] ADD FOREIGN KEY ([quiz_id]) REFERENCES [Quiz] ([quiz_id])
GO

ALTER TABLE [CourseStudyPlan] ADD FOREIGN KEY ([course_id]) REFERENCES [Course] ([course_id])
GO

ALTER TABLE [CourseStudyPlan] ADD FOREIGN KEY ([plan_id]) REFERENCES [StudyPlan] ([plan_id])
GO

ALTER TABLE [StudentStudyPlan] ADD FOREIGN KEY ([student_id]) REFERENCES [Student] ([student_id])
GO

ALTER TABLE [StudentStudyPlan] ADD FOREIGN KEY ([course_id]) REFERENCES [Course] ([course_id])
GO

ALTER TABLE [StudentStudyPlan] ADD FOREIGN KEY ([course_plan_id]) REFERENCES [CourseStudyPlan] ([course_plan_id])
GO

ALTER TABLE [StudentStudyPlan] ADD FOREIGN KEY ([previous_plan_id]) REFERENCES [CourseStudyPlan] ([course_plan_id])
GO

ALTER TABLE [StudentLessonResourceProgress] ADD FOREIGN KEY ([student_id]) REFERENCES [Student] ([student_id])
GO

ALTER TABLE [StudentLessonResourceProgress] ADD FOREIGN KEY ([lesson_resource_id]) REFERENCES [LessonResource] ([lesson_resource_id])
GO
