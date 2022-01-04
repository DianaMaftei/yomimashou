import { History } from 'history';
import AddCircleOutlineIcon from 'mdi-react/AddCircleOutlineIcon';
import AssessmentIcon from 'mdi-react/AssessmentIcon';
import BrainIcon from 'mdi-react/BrainIcon';
import SettingsIcon from 'mdi-react/SettingsIcon';
import ViewDashboardIcon from 'mdi-react/ViewDashboardIcon';
import { Link, withRouter } from 'react-router-dom';
import { isAuthenticated } from '../../service/AuthService';
import colors from '../../style/colorConstants';
import logo from './appLogo.svg';
import './drawer.scss';
import DrawerLink from './DrawerLink';


export const showDrawer = (event: Event) => {
    event.stopPropagation();
    event.preventDefault();
    const container = document.getElementById('router-container');
    if (container) {
        container.classList.remove(...container.classList);
        container.classList.add("router-container");
        container.classList.add("push-effect");
        container.classList.add("drawer-open");
    }
}

export const closeDrawer = () => {
    setTimeout(function () {
        const container = document.getElementById('router-container');
        if (container) {
            container.classList.remove(...container.classList);
            container.classList.add("router-container");
            container.classList.add("push-effect");
        }
    }, 25);
};

const getLinkColor = (pathname: string, pageUrl: string) => {
    return pathname === pageUrl ? colors.yomiWhite : colors.yomiGray500;
}

const Drawer = ({history}: DrawerProps) => {
    let pathname = history.location.pathname;
    history.listen((location) => {
        pathname = location.pathname;
    });

    return (
        <div id="drawer" key={pathname}>
            <div id="drawer-top">
                <img className="logo" src={logo} alt="logo"/>
            </div>
            <div id="drawer-center">
                <DrawerLink link="/" text="Texts" color={getLinkColor(pathname, "/")} onClick={closeDrawer}>
                    <ViewDashboardIcon size="42" color="inherit"/>
                </DrawerLink>
                <DrawerLink link="/add" text="Add Text" color={getLinkColor(pathname, "/add")} onClick={closeDrawer}>
                    <AddCircleOutlineIcon size="42" color="inherit"/>
                </DrawerLink>
                <DrawerLink link="/decks" text="Practice" color={getLinkColor(pathname, "/decks")}
                            onClick={closeDrawer}>
                    <BrainIcon size="42" color="inherit"/>
                </DrawerLink>
                <DrawerLink link="/" text="Dashboard" color={colors.yomiGray500} onClick={closeDrawer}>
                    <AssessmentIcon size="42" color={colors.yomiGray500}/>
                </DrawerLink>
            </div>
            {
                isAuthenticated() ? (
                    <div id="drawer-bottom">
                        <DrawerLink link="/" text="Settings" color={colors.yomiGray500} onClick={closeDrawer}>
                            <SettingsIcon size="42" color={colors.yomiGray500}/>
                        </DrawerLink>
                        <div>|</div>
                        <div>
                            <span>Logout</span>
                        </div>
                    </div>
                ) : (
                    <div id="drawer-bottom">
                        <Link to={"/login"} className="drawer-link" onClick={closeDrawer}>
                            <span style={{paddingRight: "20px"}}>Login</span>
                        </Link>
                        <div>|</div>
                        <Link to={"/register"} className="drawer-link" onClick={closeDrawer}>
                            <span>Register</span>
                        </Link>
                    </div>
                )
            }
        </div>
    );
};

type DrawerProps = {
    history: History
}

export default withRouter(Drawer);
