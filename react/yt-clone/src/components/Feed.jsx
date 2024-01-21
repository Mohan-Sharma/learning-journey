import { Box, Stack, Typography } from "@mui/material";
import { Sidebar }                from "./Sidebar";

export const Feed = () => (
        <Stack sx={{flexDirection: {sx: 'column', md: 'row'}}}>
            <Box sx={{ height: {sx: 'auto', md: '97svh'}, borderRight: '1px solid #3d3d3d', px: { sx: 0, md:  2}}}>
                <Sidebar />
                <Typography sx={{ color: '#fff', display: {xs: 'none', md: 'block'}}}> Copyright {new Date().getFullYear()} YT</Typography>
            </Box>
        </Stack>
)