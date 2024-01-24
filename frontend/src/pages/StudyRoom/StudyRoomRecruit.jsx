import React, {useState} from 'react';
import { styled } from '@mui/material/styles';
import {Pagination, TextField, Button, Stack, Box,List,ListItemButton,Grid,Typography,Divider, IconButton, Tooltip} from '@mui/material'
import LockIcon from '@mui/icons-material/Lock';
import LockOpenIcon from '@mui/icons-material/LockOpen';
import SearchIcon from '@mui/icons-material/Search'
import GroupAddIcon from '@mui/icons-material/GroupAdd';
import StudyRoomCreateModal from '../../components/StudyRoom/StudyRoomCreateModal';
import StudyRoomRecruitList from '../../components/StudyRoom/StudyRoomRecruitList';

// 스터디 공고 데이터
const data = [
  { title: 'JAVA 초급자 스터디 구해요', hashtag: ['#JAVA','#초급'] , state: '모집중', isPublic: false, description: '안녕하세요 JAVA 초급자 스터디원 모집합니다. 시간이나 장소는 같이 협의해보도록 해요~ 문의사항 있으신 분은 1:1 대화 주시면 답장 드릴게요!', totalCount: 10, nowCount: 2 },
  { title: '스프링 마스터 하실분', hashtag: ['#SPRING','#중급'] , state: '모집중', isPublic: true, description: '안녕하세요 JAVA 초급자 스터디원 모집합니다. 시간이나 장소는 같이 협의해보도록 해요~ 문의사항 있으신 분은 1:1 대화 주시면 답장 드릴게요!', totalCount: 10, nowCount: 5 },
  { title: 'React 초급자 모여라', hashtag: ['#REACT','#초급'] , state: '모집완료', isPublic: true, description: '안녕하세요 JAVA 초급자 스터디원 모집합니다. 시간이나 장소는 같이 협의해보도록 해요~ 문의사항 있으신 분은 1:1 대화 주시면 답장 드릴게요!', totalCount: 3, nowCount: 2 },
  { title: 'CS 뿌시기', hashtag: ['#CS','#초급'] , state: '모집완료', isPublic: false, description: '안녕하세요 JAVA 초급자 스터디원 모집합니다. 시간이나 장소는 같이 협의해보도록 해요~ 문의사항 있으신 분은 1:1 대화 주시면 답장 드릴게요!', totalCount: 5, nowCount: 2 },
  { title: '기술면접 준비방', hashtag: ['#면접','#초급'] , state: '모집중', isPublic: true, description: '안녕하세요 JAVA 초급자 스터디원 모집합니다. 시간이나 장소는 같이 협의해보도록 해요~ 문의사항 있으신 분은 1:1 대화 주시면 답장 드릴게요!', totalCount: 5, nowCount: 2 },
  { title: 'JAVA 중급자 스터디 구해요', hashtag: ['#JAVA','#중급'] , state: '모집중', isPublic: false, description: '안녕하세요 JAVA 초급자 스터디원 모집합니다. 시간이나 장소는 같이 협의해보도록 해요~ 문의사항 있으신 분은 1:1 대화 주시면 답장 드릴게요!', totalCount: 10, nowCount: 2 },
];

function generate(element) {
    return [0, 1, 2, 3, 4, 5,6 ,7].map((value) =>
      React.cloneElement(element, {
        key: value,
      }),
    );
  }
  
  const Demo = styled('div')(({ theme }) => ({
    backgroundColor: theme.palette.background.paper,
  }));

// 스터디 모집하는 페이지
export default function StudyRoomRecruit(){

    const [selectedIndex, setSelectedIndex] = React.useState(0);
    const handleListItemClick = (event, index) => {
      setSelectedIndex(index);
    };

    // 스터디 만들기 모달 오픈
    const [studyCreate, SetStudyCreate] = useState(false);
    const studyCreateOpen = () => {
      SetStudyCreate(true);
    };
    const studyCreateClose = () => {
      SetStudyCreate(false);
    };

    // 현재 페이지를 나타내는 state
    const [currentPage, setCurrentPage] = useState(1);
    // 페이지 당 항목 수
    const itemsPerPage = 5;

    // 현재 페이지에 해당하는 항목만 가져오는 함수
    const getCurrentItems = () => {
      const startIndex = (currentPage - 1) * itemsPerPage;
      const endIndex = startIndex + itemsPerPage;

      // 선택된 인덱스에 따라 모집 상태를 필터링
      const filteredData = selectedIndex === 1
      ? data.filter(study => study.state === '모집중')
      : selectedIndex === 2
        ? data.filter(study => study.state === '모집완료')
        : data;

      return filteredData.slice(startIndex, endIndex).map((study, index) => (
        <StudyRoomRecruitList
          key={index}
          study={study}
        />
      ));
    };

    return(
      <Box sx={{ display: 'flex' }}>
        {/* 사이드바 메뉴 */}
        <List sx={{ maxWidth: 128, width: "100%"}} component="nav">
          <Typography sx={{display: 'flex', justifyContent: 'center', py:1}} variant='h6' fontWeight='bold'>
            스터디 모집
          </Typography>
          <ListItemButton
            selected={selectedIndex === 0}
            onClick={(event) => handleListItemClick(event, 0)}
            sx={{ display: 'flex', justifyContent: 'center', alignItems: 'center' }}
            >
              전체
          </ListItemButton>
          <ListItemButton
            selected={selectedIndex === 1}
            onClick={(event) => handleListItemClick(event, 1)}
            sx={{ display: 'flex', justifyContent: 'center', alignItems: 'center' }}
          >
            모집중
          </ListItemButton>
          <ListItemButton
            selected={selectedIndex === 2}
            onClick={(event) => handleListItemClick(event, 2)}
            sx={{ display: 'flex', justifyContent: 'center', alignItems: 'center' }}
          >
            모집완료
          </ListItemButton>
        </List>
        <Divider />

        {/* 스터디 목록 */}
        <Grid container sx={{ display: 'flex', justifyContent: 'flex-start', alignItems: 'flex-start' }}>

          <Grid item sx={{width: '90%'}} >
            {/* 검색기능 */}
            <Stack direction="row" spacing={1} margin={1} padding={1}>
              <TextField size="small" sx={{width:"100%"}} id="outlined-basic" label="원하는 스터디를 검색해보세요!" variant="outlined" />
              {/* 검색 버튼 */}
              <Tooltip title="검색">
                <IconButton style={{ margin: 2 }}>
                  <SearchIcon fontSize='medium'/>
                </IconButton>
              </Tooltip>
              {/* 스터디 만들기 버튼 */}
              <Tooltip title="스터디 만들기">
                <IconButton style={{ margin: 2 }} onClick={studyCreateOpen}>
                  <GroupAddIcon fontSize='medium'/>
                  </IconButton>
              </Tooltip>
            </Stack>
            
            {/* StudyRoomCreateModal 컴포넌트를 사용하여 모달을 렌더링 */}
            <StudyRoomCreateModal studyCreate={studyCreate} studyCreateClose={studyCreateClose} />

            {/* 스터디 모집 공고 리스트 */}
            <Demo>
              {getCurrentItems()}
            </Demo>
            {/* 페이지네이션 */}
            <Stack sx={{
                mx: 'auto',
                justifyContent: 'center',
                alignItems: 'center',
                position: 'static', 
                bottom: 0, // 화면 하단에 고정
                padding: 2, // 원하는 패딩값 지정
                zIndex: 1, // 다른 요소 위에 표시하기 위해 zIndex 사용
              }}>
              <Pagination
                  count={Math.ceil(10 / itemsPerPage)} // 전체 페이지 수
                  color="primary"
                  page={currentPage}
                  onChange={(event, value) => setCurrentPage(value)}
                />
            </Stack>
          </Grid>
          
        </Grid>
      </Box>
    )
}